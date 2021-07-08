package com.spectrum.energyservice.energy;

import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class EnergyController {

    private final EnergyService energyService;

    enum SortField{
        ORGANIZATION,
        RELEASE_COUNT,
        TOTAL_LABOR_HOURS,
        NONE
    }

    @GetMapping("/organizations")
    public List<Organization> organizations(@RequestParam(name = "sortField") SortField sortField) {
        return energyService.getOrganizations(sortField);
    }

    @GetMapping(value = "/organizations-csv", produces = "text/csv")
    public ResponseEntity<Resource> organizationsCsv(@RequestParam(name = "sortField") SortField sortField) {
        String[] csvHeader = {
                "organization", "release_count", "total_labor_hours", "all_in_production", "licenses", "most_active_months"
        };
        ByteArrayInputStream byteArrayOutputStream;

        try (
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                CSVPrinter csvPrinter = new CSVPrinter(
                        new PrintWriter(out),
                        CSVFormat.DEFAULT.withHeader(csvHeader)
                )
        ) {
            List<Organization> organizations = energyService.getOrganizations(sortField);
            List<List<Object>> csvBody = new ArrayList<>();
            for(Organization organization : organizations){
                csvBody.add(Arrays.asList(organization.getOrganization(), organization.getRelease_count(),
                        organization.getTotal_labor_hours(), organization.isAll_in_production(), organization.getLicenses(),
                        organization.getMost_active_months()));
            }
            // populating the CSV content
            for (List<Object> record : csvBody)
                csvPrinter.printRecord(record);

            // writing the underlying stream
            csvPrinter.flush();

            byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        InputStreamResource fileInputStream = new InputStreamResource(byteArrayOutputStream);

        String csvFileName = "organizations.csv";

        // setting HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + csvFileName);
        // defining the custom Content-Type
        headers.set(HttpHeaders.CONTENT_TYPE, "text/csv");

        return new ResponseEntity<>(
                fileInputStream,
                headers,
                HttpStatus.OK
        );
    }


}
