const colors = [
  'aqua',
  'tomato',
  'aquamarine',
  'blue',
  'blueviolet',
  'chocolate',
  'cornflowerblue',
  'crimson',
  'forestgreen',
  'gold',
  'lawngreen',
  'limegreen',
  'maroon',
  'mediumvioletred',
  'orange',
  'slateblue'
];
let autoRefreshCount = 0;
let autoRefreshIntervalId = null;

let initialized = false;
const storeByIdMap = new Map();
const routePointByIdMap = new Map();

const solveButton = $('#solveButton');
const stopSolvingButton = $('#stopSolvingButton');
const routesTable = $('#routes');
const storesTable = $('#stores');

const colorById = (i) => colors[i % colors.length];
const colorByCar = (route) => route === null ? null : colorById(route.id);
const colorByStore = (store) => store === null ? null : colorById(store.id);

const fetchHeaders = {
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
  },
};

const formatDistance = (distanceInMeters) => `${Math.floor(distanceInMeters / 1000)}km ${distanceInMeters % 1000}m`;

const formatDuration = (durationInMillis) => {
  const hours = Math.floor(durationInMillis / (1000 * 60 * 60));
  const minutes = Math.floor((durationInMillis % (1000 * 60 * 60)) / (1000 * 60));
  const formattedHours = hours.toString().padStart(2, '0');
  const formattedMinutes = minutes.toString().padStart(2, '0');
  return formattedHours + ':' + formattedMinutes;
}


const getStatus = () => {
  fetch('/vrp/status', fetchHeaders)
    .then((response) => {
      if (!response.ok) {
        return handleErrorResponse('Get status failed', response);
      } else {
        return response.json().then((data) => showProblem(data));
      }
    })
    .catch((error) => handleClientError('Failed to process response', error));
};

const solve = () => {
  fetch('/vrp/solve', { ...fetchHeaders, method: 'POST' })
    .then((response) => {
      if (!response.ok) {
        return handleErrorResponse('Start solving failed', response);
      } else {
        updateSolvingStatus(true);
        autoRefreshCount = 300;
        if (autoRefreshIntervalId == null) {
          autoRefreshIntervalId = setInterval(autoRefresh, 500);
        }
      }
    })
    .catch((error) => handleClientError('Failed to process response', error));
};

const stopSolving = () => {
  fetch('/vrp/stopSolving', { ...fetchHeaders, method: 'POST' })
    .then((response) => {
      if (!response.ok) {
        return handleErrorResponse('Stop solving failed', response);
      } else {
        updateSolvingStatus(false);
        getStatus();
      }
    })
    .catch((error) => handleClientError('Failed to process response', error));
};

const formatErrorResponseBody = (body) => {
  // JSON must not contain \t (Quarkus bug)
  const json = JSON.parse(body.replace(/\t/g, '  '));
  return `${json.details}\n${json.stack}`;
};

const handleErrorResponse = (title, response) => {
  return response.text()
    .then((body) => {
      const message = `${title} (${response.status}: ${response.statusText}).`;
      const stackTrace = body ? formatErrorResponseBody(body) : '';
      showError(message, stackTrace);
    });
};

const handleClientError = (title, error) => {
  console.error(error);
  showError(`${title}.`,
    // Stack looks differently in Chrome and Firefox.
    error.stack.startsWith(error.name)
      ? error.stack
      : `${error.name}: ${error.message}\n    ${error.stack.replace(/\n/g, '\n    ')}`);
};

const showError = (message, stackTrace) => {
  const notification = $(`<div class="toast shadow rounded-lg" role="alert" style="min-width: 30rem"/>`)
    .append($(
      `<div class="toast-header bg-danger">
<strong class="mr-auto text-dark">Error</strong>
<button type="button" class="ml-2 mb-1 close" data-dismiss="toast">
<span>&times;</span>
</button>
</div>`))
    .append($(`<div class="toast-body"/>`)
      .append($(`<p/>`).text(message))
      .append($(`<pre/>`)
        .append($(`<code/>`).text(stackTrace)),
      ),
    );
  $('#notificationPanel').append(notification);
  notification.toast({ autohide: false });
  notification.toast('show');
};

const updateSolvingStatus = (solving) => {
  if (solving) {
    solveButton.hide();
    stopSolvingButton.show();
  } else {
    autoRefreshCount = 0;
    solveButton.show();
    stopSolvingButton.hide();
  }
};

const autoRefresh = () => {
  getStatus();
  autoRefreshCount--;
  if (autoRefreshCount <= 0) {
    clearInterval(autoRefreshIntervalId);
    autoRefreshIntervalId = null;
  }
};

const storePopupContent = (store, color) => `<h5>Store ${store.id}</h5>
<ul class="list-unstyled">
<li><span style="background-color: ${color}; display: inline-block; width: 12px; height: 12px; text-align: center">
</span> ${color}</li>
</ul>`;

const routePointPopupContent = (routePoint) => `<h5>Customer ${routePoint.id}</h5>
AddressTotalWeight: ${routePoint.addressTotalWeight}`;

const getStoreMarker = ({ id, mapPoint }) => {
  let marker = storeByIdMap.get(id);
  if (marker) {
    return marker;
  }
  marker = L.circleMarker(mapPoint, { radius: 2 });
  marker.addTo(storeGroup).bindPopup();
  storeByIdMap.set(id, marker);
  return marker;
};

const getCustomerMarker = ({ id, mapPoint }) => {
  let marker = routePointByIdMap.get(id);
  if (marker) {
    return marker;
  }
  marker = L.circleMarker(mapPoint, { radius: 1 });
  marker.addTo(routePointGroup).bindPopup();
  routePointByIdMap.set(id, marker);
  return marker;
};


const showProblem = ({ solution, scoreExplanation, isSolving }) => {
  if (!initialized) {
    initialized = true;
    map.fitBounds([
      [49.950243, 29.346807],
      [50.874353, 31.799505]
    ]);
  }

  // Cars
  $('[data-toggle="tooltip-load"]').tooltip('dispose');
  routesTable.children().remove();
  solution.routeList.forEach((route) => {
    const { id, car, totalDemand, routeDistanceDuration } = route;
    const percentage = totalDemand / car.weightCapacity * 100;
    const color = colorByCar(route);
    const colorIfUsed = color;
    routesTable.append(`
      <tr>
        <td>
          <i class="fas fa-crosshairs" id="crosshairs-${id}"
            style="background-color: ${colorIfUsed}; display: inline-block; width: 1rem; height: 1rem; text-align: center">
          </i>
        </td>
        <td>Car ${id}</td>
        <td>
          <div class="progress" data-toggle="tooltip-load" data-placement="left" data-html="true"
            title="Cargo: ${totalDemand}<br/>TotalWeight: ${car.weightCapacity}">
            <div class="progress-bar" role="progressbar" style="width: ${percentage}%">${totalDemand}/${car.weightCapacity}</div>
          </div>
        </td>
        <td>${formatDistance(routeDistanceDuration.distance)}</td>
        <td>${formatDuration(routeDistanceDuration.duration)}</td>
      </tr>`);
  });
  $('[data-toggle="tooltip-load"]').tooltip();
  // Stores
  storesTable.children().remove();
  solution.storeList.forEach((store) => {
    const { id } = store;
    const color = colorByStore(store);
    // const icon = defaultIcon;
    const marker = getStoreMarker(store);
    // marker.setIcon(icon);
    marker.setPopupContent(storePopupContent(store, color));
    storesTable.append(`<tr>
      <td><i class="fas fa-crosshairs" id="crosshairs-${id}"
      style="background-color: ${color}; display: inline-block; width: 1rem; height: 1rem; text-align: center">
      </i></td><td>Store ${id}</td>
      </tr>`);
  });
  // Customers
  solution.routePointList.forEach((routePoint) => {
    getCustomerMarker(routePoint).setPopupContent(routePointPopupContent(routePoint));
  });


  // Route
  routeForCarGroup.clearLayers();

  // solution.routeList.forEach((route) => {
  //   L.polyline(route.routeForCar, { color: colorByCar(route) }).addTo(routeForCarGroup);
  // });

// Without API GraphHopper
//   solution.routeList.forEach((route) => {
//     route.routeForCar.slice(0, -1).forEach((point, index) => {
//       const color = colorByCar(route);
//
//       const markerIcon = L.divIcon({
//         className: 'spike-marker',
//         html: `<div class="spike-icon" style="background-color: ${color}"></div>
//                <div class="marker-label">${index + 1}</div>`,
//         iconSize: [20, 40],
//         iconAnchor: [10, 40],
//       });
//
//       L.marker(point, {
//         icon: markerIcon,
//       }).addTo(routeForCarGroup);
//     });
//   });


  // With API GraphHopper
  const GH_API_KEY = '28656985-1f90-4553-98b5-593ae4add77e';
  const GH_API_URL = `https://graphhopper.com/api/1/route`;

  function decodeCoordinates(encodedCoordinates) {
    const coordinates = [];
    let index = 0;
    let lat = 0;
    let lng = 0;

    while (index < encodedCoordinates.length) {
      let shift = 0;
      let result = 0;
      let byte;

      do {
        byte = encodedCoordinates.charCodeAt(index++) - 63;
        result |= (byte & 0x1f) << shift;
        shift += 5;
      } while (byte >= 0x20);

      const deltaLat = (result & 1) ? ~(result >> 1) : (result >> 1);
      lat += deltaLat;

      shift = 0;
      result = 0;

      do {
        byte = encodedCoordinates.charCodeAt(index++) - 63;
        result |= (byte & 0x1f) << shift;
        shift += 5;
      } while (byte >= 0x20);

      const deltaLng = (result & 1) ? ~(result >> 1) : (result >> 1);
      lng += deltaLng;

      const decodedCoordinate = [lat * 1e-5, lng * 1e-5];
      coordinates.push(decodedCoordinate);
    }

    return coordinates;
  }

  solution.routeList.forEach(async (route) => {

    route.routeForCar.slice(0, -1).forEach((point, index) => {
      const color = colorByCar(route);

      const markerIcon = L.divIcon({
        className: 'spike-marker',
        html: `<div class="spike-icon" style="background-color: ${color}"></div>
               <div class="marker-label">${index + 1}</div>`,
        iconSize: [20, 40],
        iconAnchor: [10, 40],
      });

      L.marker(point, {
        icon: markerIcon,
      }).addTo(routeForCarGroup);
    });

    const coordinates = [];
    for (let i = 0; i < route.routeForCar.length - 1; i++) {
      const startPoint = route.routeForCar[i];
      const endPoint = route.routeForCar[i + 1];

      await new Promise((resolve) => setTimeout(resolve, 2000));

      const response = await fetch(`${GH_API_URL}?point=${startPoint[0]},${startPoint[1]}&point=${endPoint[0]},${endPoint[1]}&vehicle=car&key=${GH_API_KEY}`);
      const data = await response.json();
      const routeForCarCoordinates = data.paths[0].points;
      const decodedPoints = decodeCoordinates(routeForCarCoordinates);
      coordinates.push(...decodedPoints);
    }
    L.polyline(coordinates, { color: colorByCar(route) }).addTo(routeForCarGroup);
  });



  // Summary
  $('#score').text(solution.score);
  $('#scoreInfo').text(scoreExplanation);
  $('#distance').text(formatDistance(solution.distanceMeters));
  updateSolvingStatus(isSolving);
};

const map = L.map('map', { doubleClickZoom: false }).setView([51.505, -0.09], 13);
map.whenReady(getStatus);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
  maxZoom: 19,
  attribution: '&copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors',
}).addTo(map);

const routePointGroup = L.layerGroup().addTo(map);
const storeGroup = L.layerGroup().addTo(map);
const routeForCarGroup = L.layerGroup().addTo(map);

solveButton.click(solve);
stopSolvingButton.click(stopSolving);

updateSolvingStatus();
$('[data-toggle="tooltip"]').tooltip();
