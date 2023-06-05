package com.olshevchenko.optaplanner.entity.solver;

import com.olshevchenko.optaplanner.entity.Customer;
import com.olshevchenko.optaplanner.entity.Store;
import com.olshevchenko.optaplanner.entity.RoutingSolution;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorterWeightFactory;

import java.util.Comparator;

import static java.util.Comparator.comparingDouble;
import static java.util.Comparator.comparingLong;

/**
 * On large datasets, the constructed solution looks like pizza slices.
 */
public class StoreAngleCustomerDifficultyWeightFactory implements SelectionSorterWeightFactory<RoutingSolution, Customer> {

    @Override
    public DepotAngleCustomerDifficultyWeight createSorterWeight(RoutingSolution routingSolution, Customer customer) {
        Store store = routingSolution.getStoreList().get(0);
        return new DepotAngleCustomerDifficultyWeight(customer,
                customer.getMapPoint().getAngle(store.getMapPoint()),
                customer.getMapPoint().getDistanceTo(store.getMapPoint())
                        + store.getMapPoint().getDistanceTo(customer.getMapPoint()));
    }

    public static class DepotAngleCustomerDifficultyWeight
            implements Comparable<DepotAngleCustomerDifficultyWeight> {

        private static final Comparator<DepotAngleCustomerDifficultyWeight> COMPARATOR = comparingDouble(
                (DepotAngleCustomerDifficultyWeight weight) -> weight.depotAngle)
                .thenComparingLong(weight -> weight.depotRoundTripDistance) // Ascending (further from the depot are more difficult)
                .thenComparing(weight -> weight.customer, comparingLong(Customer::getId));

        private final Customer customer;
        private final double depotAngle;
        private final long depotRoundTripDistance;

        public DepotAngleCustomerDifficultyWeight(Customer customer, double depotAngle, long depotRoundTripDistance) {
            this.customer = customer;
            this.depotAngle = depotAngle;
            this.depotRoundTripDistance = depotRoundTripDistance;
        }

        @Override
        public int compareTo(DepotAngleCustomerDifficultyWeight other) {
            return COMPARATOR.compare(this, other);
        }
    }
}
