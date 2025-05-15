package eci.cvds.tdd.module.sportLoan.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eci.cvds.tdd.module.sportLoan.model.DTO.LoanRequest;
import eci.cvds.tdd.module.sportLoan.model.DTO.ReturnDetails;
import eci.cvds.tdd.module.sportLoan.model.Loan;
import eci.cvds.tdd.module.sportLoan.service.LoanService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanController.class)
public class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanService loanService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateLoanSuccess() throws Exception {
        LoanRequest request = new LoanRequest(); // Rellena si es necesario
        Loan loan = new Loan(); loan.setId("123");

        Mockito.when(loanService.createLoan(any())).thenReturn(loan);

        mockMvc.perform(post("/api/loans/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("123"));
    }

    @Test
    void testCreateLoanFailure() throws Exception {
        LoanRequest request = new LoanRequest();

        Mockito.when(loanService.createLoan(any()))
                .thenThrow(new RuntimeException("Loan creation failed"));

        mockMvc.perform(post("/api/loans/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Loan creation failed"));
    }

    @Test
    void testReturnLoan() throws Exception {
        ReturnDetails details = new ReturnDetails();
        Loan loan = new Loan(); loan.setId("456");

        Mockito.when(loanService.returnLoan(any())).thenReturn(loan);

        mockMvc.perform(post("/api/loans/return")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(details)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("456"));
    }

    @Test
    void testGetLoanByIdFound() throws Exception {
        Loan loan = new Loan(); loan.setId("789");

        Mockito.when(loanService.getLoanById("789")).thenReturn(loan);

        mockMvc.perform(get("/api/loans/789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("789"));
    }

    @Test
    void testGetLoanByIdNotFound() throws Exception {
        Mockito.when(loanService.getLoanById("not-exist")).thenReturn(null);

        mockMvc.perform(get("/api/loans/not-exist"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testListLoansByUser() throws Exception {
        Loan loan1 = new Loan(); loan1.setId("l1");
        Loan loan2 = new Loan(); loan2.setId("l2");

        Mockito.when(loanService.listLoansByUser("user123")).thenReturn(List.of(loan1, loan2));

        mockMvc.perform(get("/api/loans/user/user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("l1"))
                .andExpect(jsonPath("$[1].id").value("l2"));
    }
}
