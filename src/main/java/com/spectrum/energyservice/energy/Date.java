package com.spectrum.energyservice.energy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Date {

    private String created;
    private String metadataLastUpdated;
}
