package com.spectrum.energyservice.energy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Organization {
    String organization;
    int release_count;
    double total_labor_hours;
    boolean all_in_production;
    List<String> licenses;
    List<Integer> most_active_months;
}
