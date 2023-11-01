package kr.co.zerobase.stock.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import kr.co.zerobase.stock.model.Company;
import kr.co.zerobase.stock.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/company")
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword) {

        return ResponseEntity.ok(companyService.getCompanyNamesByKeyword(keyword));
    }

    @GetMapping
    @PreAuthorize("hasRole('READ')")
    public ResponseEntity<Page<Company>> searchCompany(final Pageable pageable) {
        return ResponseEntity.ok(companyService.getAllCompany(pageable));
    }

    @PostMapping
    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<Company> addCompany(@RequestBody @Valid @NotEmpty Company request) {
        String ticker = request.getTicker();
        if (ObjectUtils.isEmpty(ticker)) {
            throw new RuntimeException("ticker is empty");
        }

        Company company = companyService.save(ticker);
        companyService.addAutocompleteKeyword(company.getName());
        return ResponseEntity.ok(company);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCompany(@PathVariable String ticker) {
        return null;
    }
}
