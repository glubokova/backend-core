package ru.mentee.power.crm.servlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.mentee.power.crm.model.Lead;
import ru.mentee.power.crm.model.LeadStatus;
import ru.mentee.power.crm.service.LeadService;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

class LeadListServletTest {

    private LeadListServlet servlet;
    private LeadService leadService;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ServletContext context;

    @BeforeEach
    void setUp() {
        servlet = new LeadListServlet();
        leadService = mock(LeadService.class);
        response = mock(HttpServletResponse.class);
        request = mock(HttpServletRequest.class);
        context = mock(ServletContext.class);
        when(request.getServletContext()).thenReturn(context);
    }

    @Test
    void shouldReturnHtmlTableWhenDoGetCalled() throws Exception {
        Lead lead = mock(Lead.class);
        when(lead.email()).thenReturn("test@mail.com");
        when(lead.company()).thenReturn("TEST");
        when(lead.status()).thenReturn(LeadStatus.NEW);

        when(leadService.findAll()).thenReturn(List.of(lead));
        when(context.getAttribute("leadService")).thenReturn(leadService);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        servlet.init();
        servlet.doGet(request, response);
        verify(leadService).findAll();
        writer.flush();

        String output = stringWriter.toString();

        assertTrue(output.contains("<table"));
        assertTrue(output.contains("test@mail.com"));
        assertTrue(output.contains("TEST"));
        assertTrue(output.contains("NEW"));
    }

    @Test
    void shouldSetContentTypeToHtmlWhenDoGetCalled() throws Exception {
        when(leadService.findAll()).thenReturn(List.of());
        when(context.getAttribute("leadService")).thenReturn(leadService);
        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));

        servlet.init();
        servlet.doGet(request, response);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(response).setContentType(captor.capture());
        assertEquals("text/html; charset=UTF-8", captor.getValue());
    }
}