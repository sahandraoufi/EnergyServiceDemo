package com.spectrum.energyservice.energy;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EnergyService {

    private final static String url = "https://www.energy.gov/sites/prod/files/2020/12/f81/code-12-15-2020.json";

    public List<Organization> getOrganizations(EnergyController.SortField sortField, EnergyController.SortType sortType) {

        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Organization> organizationHashMap = new HashMap<>();
        try {
            URL jsonUrl = new URL(url);
            List<Release> releases = mapper.readValue(jsonUrl, EnergyInput.class).getReleases();

            releases.forEach(release -> {
                        if (organizationHashMap.containsKey(release.getOrganization())) {
                            Organization organization = organizationHashMap.get(release.getOrganization());
                            List<Integer> mostActive = new ArrayList<>(organization.getMost_active_months());
                            mostActive.add(Integer.parseInt(release.getDate().getMetadataLastUpdated().split("-")[1]));
                            organization.setMost_active_months(mostActive);
                            organization.setRelease_count(organization.getRelease_count() + 1);
                            organization.getLicenses().addAll(release.getPermissions().getLicenses().stream().map(License::getName).collect(Collectors.toList()));
                            organization.setTotal_labor_hours(organization.getTotal_labor_hours() + release.getLaborHours());
                            organization.setAll_in_production(release.getStatus().equalsIgnoreCase("Production") && organization.isAll_in_production());
                        } else {
                            Organization organization = Organization.builder().organization(release.getOrganization()).release_count(1)
                                    .all_in_production(release.getStatus().equalsIgnoreCase("Production"))
                                    .licenses(release.getPermissions().getLicenses().stream().map(License::getName).collect(Collectors.toList()))
                                    .total_labor_hours(release.getLaborHours())
                                    .most_active_months(List.of(Integer.parseInt(release.getDate().getMetadataLastUpdated().split("-")[1])))
                                    .build();
                            organizationHashMap.put(release.getOrganization(), organization);
                        }
                    }
            );

        } catch (IOException e) {
            e.printStackTrace();
        }

        //sorting the result based on selected field and selected type(ASCENDING or DESCENDING)
        switch (sortField) {
            case NONE:
                return new ArrayList<>(organizationHashMap.values());
            case ORGANIZATION:
                if (sortType.equals(EnergyController.SortType.ASCENDING)) {
                    return organizationHashMap.values().stream().sorted(Comparator.comparing(Organization::getOrganization))
                            .collect(Collectors.toList());
                } else {
                    return organizationHashMap.values().stream().sorted(Comparator.comparing(Organization::getOrganization).reversed())
                            .collect(Collectors.toList());
                }
            case RELEASE_COUNT:
                if (sortType.equals(EnergyController.SortType.ASCENDING)) {
                    return organizationHashMap.values().stream().sorted(Comparator.comparing(Organization::getRelease_count).reversed())
                            .collect(Collectors.toList());
                } else {
                    return organizationHashMap.values().stream().sorted(Comparator.comparing(Organization::getRelease_count))
                            .collect(Collectors.toList());
                }
            case TOTAL_LABOR_HOURS:
                if (sortType.equals(EnergyController.SortType.ASCENDING)) {
                    return organizationHashMap.values().stream().sorted(Comparator.comparing(Organization::getTotal_labor_hours).reversed())
                            .collect(Collectors.toList());
                } else {
                    return organizationHashMap.values().stream().sorted(Comparator.comparing(Organization::getTotal_labor_hours))
                            .collect(Collectors.toList());
                }
            default:
                return new ArrayList<>();
        }

    }

}
