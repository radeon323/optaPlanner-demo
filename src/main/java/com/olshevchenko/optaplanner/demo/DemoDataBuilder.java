package com.olshevchenko.optaplanner.demo;

import com.olshevchenko.optaplanner.entity.*;
import com.olshevchenko.optaplanner.utils.DistanceCalculator;
import com.olshevchenko.optaplanner.utils.EuclideanDistanceCalculator;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor
public class DemoDataBuilder {

    private static final AtomicLong sequence = new AtomicLong();
    private final DistanceCalculator distanceCalculator = new EuclideanDistanceCalculator();
    private int minDemand;
    private int maxDemand;

    private final List<Store> storeList = new ArrayList<>();
    private final List<Car> carList = new ArrayList<>();
    private final List<Customer> customerList = new ArrayList<>();
    

    public static DemoDataBuilder builder() {
        return new DemoDataBuilder();
    }

    public DemoDataBuilder setMinDemand(int minDemand) {
        this.minDemand = minDemand;
        return this;
    }

    public DemoDataBuilder setMaxDemand(int maxDemand) {
        this.maxDemand = maxDemand;
        return this;
    }
    

    public RoutingSolution build() {
        if (minDemand < 1) {
            throw new IllegalStateException("minDemand (" + minDemand + ") must be greater than zero.");
        }
        if (maxDemand < 1) {
            throw new IllegalStateException("maxDemand (" + maxDemand + ") must be greater than zero.");
        }
        if (minDemand >= maxDemand) {
            throw new IllegalStateException("maxDemand (" + maxDemand + ") must be greater than minDemand ("
                    + minDemand + ").");
        }

        String name = "GrandeDolce";

        Random random = new Random(0);
        PrimitiveIterator.OfInt demand = random.ints(minDemand, maxDemand + 1).iterator();
        
        storeList.add(new Store(1, new MapPoint(1, 50.08, 29.89)));

        Car car1 = new Car(sequence.incrementAndGet(), 650, storeList.get(0));
        Car car2 = new Car(sequence.incrementAndGet(), 800, storeList.get(0));
        Car car3 = new Car(sequence.incrementAndGet(), 800, storeList.get(0));
        Car car4 = new Car(sequence.incrementAndGet(), 1100, storeList.get(0));
        Car car5 = new Car(sequence.incrementAndGet(), 2000, storeList.get(0));
        Car car6 = new Car(sequence.incrementAndGet(), 1900, storeList.get(0));
        Car car7 = new Car(sequence.incrementAndGet(), 800, storeList.get(0));
        carList.addAll(List.of(car1, car2, car3, car4, car5, car6, car7));

        Customer customer1 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.1640041,30.673291), demand.nextInt());
        Customer customer2 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.4108479,30.6032353), demand.nextInt());
        Customer customer3 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.4359328,30.4853366), demand.nextInt());
        Customer customer4 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.4741431,30.4941509), demand.nextInt());
        Customer customer5 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.5942085,30.4665496), demand.nextInt());
        Customer customer6 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.5503328,30.1498909), demand.nextInt());
        Customer customer7 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.4369708,30.3470976), demand.nextInt());
        Customer customer8 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.3461146,30.324353), demand.nextInt());
        Customer customer9 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.3495305,30.4746035), demand.nextInt());
        Customer customer10 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.3413407,30.5473056), demand.nextInt());

        Customer customer11 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.4108479,30.6032353), demand.nextInt());
        Customer customer12 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.4106276,30.5981905), demand.nextInt());
        Customer customer13 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.3935623,30.6648023), demand.nextInt());
        Customer customer14 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.3700271,30.91303), demand.nextInt());
        Customer customer15 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.3575013,30.9285713), demand.nextInt());
        Customer customer16 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.6008699,30.7546009), demand.nextInt());
        Customer customer17 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.5089634,30.6068443), demand.nextInt());
        Customer customer18 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.493718,30.5749959), demand.nextInt());
        Customer customer19 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.4689438,30.6425254), demand.nextInt());
        Customer customer20 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.4691785,30.6430593), demand.nextInt());

        Customer customer21 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.4590832,30.6352948), demand.nextInt());
        Customer customer22 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.4310444,30.6161278), demand.nextInt());
        Customer customer23 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.422031,30.5383836), demand.nextInt());
        Customer customer24 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.4135805,30.5419417), demand.nextInt());
        Customer customer25 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.4155115,30.5226263), demand.nextInt());
        Customer customer26 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.3970949,30.3183628), demand.nextInt());
        Customer customer27 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.4282051,30.3608289), demand.nextInt());
        Customer customer28 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.4173288,30.3884417), demand.nextInt());
        Customer customer29 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.462317,30.4298805), demand.nextInt());
        Customer customer30 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.5148716,30.4167947), demand.nextInt());

        Customer customer31 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.51428,30.4210107), demand.nextInt());
        Customer customer32 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.5043388,30.4710591), demand.nextInt());
        Customer customer33 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.5280539,30.5010323), demand.nextInt());
        Customer customer34 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.4944968,30.5199339), demand.nextInt());
        Customer customer35 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.4718563,30.5166588), demand.nextInt());
        Customer customer36 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.4736973,30.4966593), demand.nextInt());
        Customer customer37 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.4687172,30.5023158), demand.nextInt());
        Customer customer38 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.4614768,30.5180501), demand.nextInt());
        Customer customer39 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.464375,30.5192751), demand.nextInt());
        Customer customer40 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.4263342,30.5133754), demand.nextInt());

        Customer customer41 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.4397796,30.5186607), demand.nextInt());
        Customer customer42 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.4524994,30.4993099), demand.nextInt());
        Customer customer43 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.4485918,30.4891998), demand.nextInt());
        Customer customer44 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.4266975,30.4743667), demand.nextInt());
        Customer customer45 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.3919675,30.4772963), demand.nextInt());
        Customer customer46 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.3831815,30.4552023), demand.nextInt());
        Customer customer47 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.5755587,30.0676775), demand.nextInt());
        Customer customer48 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.5191956,30.2095767), demand.nextInt());
        Customer customer49 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.4648601,30.3263898), demand.nextInt());
        Customer customer50 = new Customer(sequence.incrementAndGet(), new MapPoint(sequence.incrementAndGet(), 50.4421405,30.288724), demand.nextInt());

        customerList.addAll(List.of(customer1, customer2, customer3, customer4, customer5, customer6, customer7, customer8, customer9, customer10,
                customer11, customer12, customer13, customer14, customer15, customer16, customer17, customer18, customer19, customer20,
                customer21, customer22, customer23, customer24, customer25, customer26, customer27, customer28, customer29, customer30,
                customer31, customer32, customer33, customer34, customer35, customer36, customer37, customer38, customer39, customer40,
                customer41, customer42, customer43, customer44, customer45, customer46, customer47, customer48, customer49, customer50));


        List<MapPoint> locationList = Stream.concat(
                        customerList.stream().map(Customer::getMapPoint),
                        storeList.stream().map(Store::getMapPoint))
                .collect(Collectors.toList());

        distanceCalculator.initDistanceMaps(locationList);

        return new RoutingSolution(name, locationList,
                storeList, carList, customerList);
    }
}