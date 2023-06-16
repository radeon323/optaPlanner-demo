package com.olshevchenko.optaplanner.repository;

import com.olshevchenko.optaplanner.entity.MapPoint;
import com.olshevchenko.optaplanner.entity.RoutePoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.Random;

@Repository
public class RoutePointRepository {

    @Transactional
    public List<RoutePoint> findAll() {
        PrimitiveIterator.OfInt demand = new Random(0).ints(25, 220).iterator();

        RoutePoint routePoint1 = new RoutePoint(2, new MapPoint(2, 50.1640041,30.673291), demand.nextInt());
        RoutePoint routePoint2 = new RoutePoint(3, new MapPoint(3, 50.4108479,30.6032353), demand.nextInt());
        RoutePoint routePoint3 = new RoutePoint(4, new MapPoint(4, 50.4359328,30.4853366), demand.nextInt());
        RoutePoint routePoint4 = new RoutePoint(5, new MapPoint(5, 50.4741431,30.4941509), demand.nextInt());
        RoutePoint routePoint5 = new RoutePoint(6, new MapPoint(6, 50.5942085,30.4665496), demand.nextInt());
        RoutePoint routePoint6 = new RoutePoint(7, new MapPoint(7, 50.5503328,30.1498909), demand.nextInt());
        RoutePoint routePoint7 = new RoutePoint(8, new MapPoint(8, 50.4369708,30.3470976), demand.nextInt());
        RoutePoint routePoint8 = new RoutePoint(9, new MapPoint(9, 50.3461146,30.324353), demand.nextInt());
        RoutePoint routePoint9 = new RoutePoint(10, new MapPoint(10, 50.3495305,30.4746035), demand.nextInt());
        RoutePoint routePoint10 = new RoutePoint(11, new MapPoint(11, 50.3413407,30.5473056), demand.nextInt());

        RoutePoint routePoint11 = new RoutePoint(12, new MapPoint(12, 50.4108479,30.6032353), demand.nextInt());
        RoutePoint routePoint12 = new RoutePoint(13, new MapPoint(13, 50.4106276,30.5981905), demand.nextInt());
        RoutePoint routePoint13 = new RoutePoint(14, new MapPoint(14, 50.3935623,30.6648023), demand.nextInt());
        RoutePoint routePoint14 = new RoutePoint(15, new MapPoint(15, 50.3700271,30.91303), demand.nextInt());
        RoutePoint routePoint15 = new RoutePoint(16, new MapPoint(16, 50.3575013,30.9285713), demand.nextInt());
        RoutePoint routePoint16 = new RoutePoint(17, new MapPoint(17, 50.6008699,30.7546009), demand.nextInt());
        RoutePoint routePoint17 = new RoutePoint(18, new MapPoint(18, 50.5089634,30.6068443), demand.nextInt());
        RoutePoint routePoint18 = new RoutePoint(19, new MapPoint(19, 50.493718,30.5749959), demand.nextInt());
        RoutePoint routePoint19 = new RoutePoint(20, new MapPoint(20, 50.4689438,30.6425254), demand.nextInt());
        RoutePoint routePoint20 = new RoutePoint(21, new MapPoint(21, 50.4691785,30.6430593), demand.nextInt());

        RoutePoint routePoint21 = new RoutePoint(22, new MapPoint(22, 50.4590832,30.6352948), demand.nextInt());
        RoutePoint routePoint22 = new RoutePoint(23, new MapPoint(23, 50.4310444,30.6161278), demand.nextInt());
        RoutePoint routePoint23 = new RoutePoint(24, new MapPoint(24, 50.422031,30.5383836), demand.nextInt());
        RoutePoint routePoint24 = new RoutePoint(25, new MapPoint(25, 50.4135805,30.5419417), demand.nextInt());
        RoutePoint routePoint25 = new RoutePoint(26, new MapPoint(26, 50.4155115,30.5226263), demand.nextInt());
        RoutePoint routePoint26 = new RoutePoint(27, new MapPoint(27, 50.3970949,30.3183628), demand.nextInt());
        RoutePoint routePoint27 = new RoutePoint(28, new MapPoint(28, 50.4282051,30.3608289), demand.nextInt());
        RoutePoint routePoint28 = new RoutePoint(29, new MapPoint(29, 50.4173288,30.3884417), demand.nextInt());
        RoutePoint routePoint29 = new RoutePoint(30, new MapPoint(30, 50.462317,30.4298805), demand.nextInt());
        RoutePoint routePoint30 = new RoutePoint(31, new MapPoint(31, 50.5148716,30.4167947), demand.nextInt());

        RoutePoint routePoint31 = new RoutePoint(32, new MapPoint(32, 50.51428,30.4210107), demand.nextInt());
        RoutePoint routePoint32 = new RoutePoint(33, new MapPoint(33, 50.5043388,30.4710591), demand.nextInt());
        RoutePoint routePoint33 = new RoutePoint(34, new MapPoint(34, 50.5280539,30.5010323), demand.nextInt());
        RoutePoint routePoint34 = new RoutePoint(35, new MapPoint(35, 50.4944968,30.5199339), demand.nextInt());
        RoutePoint routePoint35 = new RoutePoint(36, new MapPoint(36, 50.4718563,30.5166588), demand.nextInt());
        RoutePoint routePoint36 = new RoutePoint(37, new MapPoint(37, 50.4736973,30.4966593), demand.nextInt());
        RoutePoint routePoint37 = new RoutePoint(38, new MapPoint(38, 50.4687172,30.5023158), demand.nextInt());
        RoutePoint routePoint38 = new RoutePoint(39, new MapPoint(39, 50.4614768,30.5180501), demand.nextInt());
        RoutePoint routePoint39 = new RoutePoint(40, new MapPoint(40, 50.464375,30.5192751), demand.nextInt());
        RoutePoint routePoint40 = new RoutePoint(41, new MapPoint(41, 50.4263342,30.5133754), demand.nextInt());

        RoutePoint routePoint41 = new RoutePoint(42, new MapPoint(42, 50.4397796,30.5186607), demand.nextInt());
        RoutePoint routePoint42 = new RoutePoint(43, new MapPoint(43, 50.4524994,30.4993099), demand.nextInt());
        RoutePoint routePoint43 = new RoutePoint(44, new MapPoint(44, 50.4485918,30.4891998), demand.nextInt());
        RoutePoint routePoint44 = new RoutePoint(45, new MapPoint(45, 50.4266975,30.4743667), demand.nextInt());
        RoutePoint routePoint45 = new RoutePoint(46, new MapPoint(46, 50.3919675,30.4772963), demand.nextInt());
        RoutePoint routePoint46 = new RoutePoint(47, new MapPoint(47, 50.3831815,30.4552023), demand.nextInt());
        RoutePoint routePoint47 = new RoutePoint(48, new MapPoint(48, 50.5755587,30.0676775), demand.nextInt());
        RoutePoint routePoint48 = new RoutePoint(49, new MapPoint(49, 50.5191956,30.2095767), demand.nextInt());
        RoutePoint routePoint49 = new RoutePoint(50, new MapPoint(50, 50.4648601,30.3263898), demand.nextInt());
        RoutePoint routePoint50 = new RoutePoint(51, new MapPoint(51, 50.4421405,30.288724), demand.nextInt());

        return new ArrayList<>(List.of(routePoint1, routePoint2, routePoint3, routePoint4, routePoint5, routePoint6, routePoint7, routePoint8, routePoint9, routePoint10,
                routePoint11, routePoint12, routePoint13, routePoint14, routePoint15, routePoint16, routePoint17, routePoint18, routePoint19, routePoint20,
                routePoint21, routePoint22, routePoint23, routePoint24, routePoint25, routePoint26, routePoint27, routePoint28, routePoint29, routePoint30,
                routePoint31, routePoint32, routePoint33, routePoint34, routePoint35, routePoint36, routePoint37, routePoint38, routePoint39, routePoint40,
                routePoint41, routePoint42, routePoint43, routePoint44, routePoint45, routePoint46, routePoint47, routePoint48, routePoint49, routePoint50));
    }
}
