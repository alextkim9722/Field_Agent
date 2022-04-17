package learn.field_agent.domain;

import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SecurityClearanceServiceTest {
    @Autowired
    SecurityClearanceService service;

    @MockBean
    SecurityClearanceRepository repository;

    @Test
    void shouldfindById() {
        // pass-through test, probably not useful
        SecurityClearance expected = makeSecurityClearance();
        when(repository.findById(1)).thenReturn(expected);
        SecurityClearance actual = service.findById(1);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWhenNameIsNull() {
        SecurityClearance securityClearance = makeSecurityClearance();
        Result<SecurityClearance> result = service.add(securityClearance);
        assertEquals(ResultType.INVALID, result.getType());

        securityClearance.setSecurityClearanceId(0);
        securityClearance.setName(null);
        result = service.add(securityClearance);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddWhenValid() {
        SecurityClearance expected = makeSecurityClearance();
        SecurityClearance arg = makeSecurityClearance();
        arg.setSecurityClearanceId(0);

        when(repository.add(arg)).thenReturn(expected);
        Result<SecurityClearance> result = service.add(arg);
        assertEquals(ResultType.SUCCESS, result.getType());

        assertEquals(expected, result.getPayload());
    }

    @Test
    void shouldNotAddWhenNameIsDuplicate() {
        SecurityClearance securityClearanceA = makeSecurityClearance();
        SecurityClearance securityClearanceB = makeSecurityClearance();
        securityClearanceB.setSecurityClearanceId(2);

        service.add(securityClearanceA);
        Result<SecurityClearance> result = service.add(securityClearanceB);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotUpdateWhenDoesNotExist() {
        SecurityClearance securityClearance = makeSecurityClearance();
        securityClearance.setSecurityClearanceId(2);

        Result<SecurityClearance> result = service.update(securityClearance);
        assertEquals(ResultType.NOT_FOUND, result.getType());
    }

    @Test
    void shouldNotDeleteWhenDoesNotExist() {
        Result<SecurityClearance> result = service.deleteById(2);
        assertEquals(ResultType.NOT_FOUND, result.getType());
    }

    SecurityClearance makeSecurityClearance() {
        SecurityClearance securityClearance = new SecurityClearance();
        securityClearance.setSecurityClearanceId(1);
        securityClearance.setName("O5");
        return securityClearance;
    }
}
