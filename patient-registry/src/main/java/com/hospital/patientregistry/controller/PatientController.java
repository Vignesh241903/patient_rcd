package com.hospital.patientregistry.controller;

import com.hospital.patientregistry.dto.PatientRecord;
import com.hospital.patientregistry.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PatientController {

    private final PatientService service;

    public PatientController(PatientService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("total", service.countPatients());
        model.addAttribute("admitted", service.countByStatus("ADMITTED"));
        model.addAttribute("outpatients", service.countByStatus("OUTPATIENT"));
        model.addAttribute("today", service.countRegisteredToday());
        model.addAttribute("recent", service.recent());
        return "dashboard";
    }

    @GetMapping("/patients")
    public String list(@RequestParam(defaultValue = "") String q,
                       @RequestParam(defaultValue = "") String status,
                       Model model) {
        model.addAttribute("records", service.search(q, status));
        model.addAttribute("q", q);
        model.addAttribute("status", status);
        return "patients";
    }

    @GetMapping("/patients/new")
    public String newForm(Model model) {
        model.addAttribute("record", new PatientRecord());
        return "patient-form";
    }

    @PostMapping("/patients")
    public String create(@Valid @ModelAttribute("record") PatientRecord record,
                         BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "patient-form";
        }
        service.register(record);
        ra.addFlashAttribute("message", "Patient " + record.getFullName() + " registered.");
        return "redirect:/patients";
    }

    @GetMapping("/patients/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("record",
                service.findRecord(id).orElseThrow(() -> new IllegalArgumentException("Patient not found: " + id)));
        return "patient-form";
    }

    @PostMapping("/patients/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("record") PatientRecord record,
                         BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "patient-form";
        }
        service.update(id, record)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found: " + id));
        ra.addFlashAttribute("message", "Patient " + record.getFullName() + " updated.");
        return "redirect:/patients";
    }

    @PostMapping("/patients/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("message", "Patient record deleted.");
        return "redirect:/patients";
    }
}
