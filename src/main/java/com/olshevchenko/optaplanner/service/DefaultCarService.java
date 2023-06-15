package com.olshevchenko.optaplanner.service;

import com.olshevchenko.optaplanner.entity.Car;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultCarService {
    @Transactional(readOnly = true)
    public List<Car> findAll() {
        Car car1 = new Car(1,"Мерседес Віто Mersedes Vito", true, false,"АІ6399ЕР", 1100,9, false);
        Car car2 = new Car(2,"Мерседес Віто Mersedes Vito", true, false,"АІ1649ЕР", 800,9, false);
        Car car3 = new Car(3,"Mersedes-Benz 316 СDІ", true, false,"AI0992ЕР", 1450,11, false);
        Car car4 = new Car(4,"Mersedes-Benz", true, true,"АІ6349HA", 800,11, false);
        Car car5 = new Car(5,"Mersedes Vito", true, false,"АІ7860НА", 800,9, false);
        Car car6 = new Car(6,"Mersedes-Benz Sprinter 513CDI", true, true,"AI3097OB", 2000,13, false);
        Car car7 = new Car(7,"Mersedes-Benz Sprinter 314 CDI", true, false,"AI1060ОА", 2500,11, false);
        Car car8 = new Car(8,"Renault Kangoo", true, false,"АІ3256КН", 650,6, false);
        return List.of(car1, car2, car3, car4, car5, car6, car7, car8);
    }
}
