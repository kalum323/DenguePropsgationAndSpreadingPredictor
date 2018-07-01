/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.final_project.dpasp.service;

import edu.final_project.dpasp.repository.MohPatientsDetailRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author kalum
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MohPatientsDetailService {

    @Autowired
    private MohPatientsDetailRepository mohPatientsDetailRepository;

    public List<Object[]> findByDistrictAndDateRange(Integer districts, String month, Integer week) {
        if (week != 5) {
            return mohPatientsDetailRepository.findByDistrictAndDateRange(districts, month, week);
        } else {
            return mohPatientsDetailRepository.findByDistrictAndDateRange(districts, month);
        }
    }

    public List<Double> predictRisk(Integer districts, Integer moh, String date) {
        List<Double> list = new ArrayList<>();

        Integer noOfPatients = mohPatientsDetailRepository.getPatientsCount(districts, moh, date);
        Integer noOfpopulation = mohPatientsDetailRepository.getPopulationCount(districts, moh);

        Double previousProbability = noOfPatients.doubleValue() / noOfpopulation.doubleValue();
        Integer noOfChangeGpsPatients = mohPatientsDetailRepository.changeGpsCount(moh, date);
        Double currentProbability = previousProbability * noOfpopulation.doubleValue() + previousProbability*noOfChangeGpsPatients.doubleValue() / noOfpopulation.doubleValue() + noOfChangeGpsPatients.doubleValue();

        list.add(previousProbability);
        list.add(currentProbability);
        System.out.println("Previous Probability");
        System.out.println(previousProbability);
        System.out.println("No Of Population");
        System.out.println(noOfpopulation);
        System.out.println("No Of Change Gps Patients Count");
        System.out.println(noOfChangeGpsPatients);
        System.out.println("Previous Probability");
        System.out.println(previousProbability);
        System.out.println("Current Probability");
        System.out.println(currentProbability);
        return list;
    }
}
