package com.spectrum.energyservice;

import com.spectrum.energyservice.energy.EnergyController;
import com.spectrum.energyservice.energy.EnergyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EnergyServiceTest {

    @Autowired
    EnergyService energyService;

    @Test
    public void getOrganizations()
    {
        energyService.getOrganizations(EnergyController.SortField.ORGANIZATION, EnergyController.SortType.ASCENDING);
    }
}
