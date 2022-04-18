package learn.field_agent.domain;

import learn.field_agent.data.AliasRepository;
import learn.field_agent.models.Alias;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AliasServiceTest {

    @Autowired
    AliasService service;

    @MockBean
    AliasRepository repository;

    @Test
    void shouldNotAddWhenNoName() {
        Alias alias = makeAlias();
        alias.setName("   ");

        Result<Alias> actual = service.add(alias);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldNotAddWhenDuplicateExists() {
        Alias alias = makeAlias();
        alias.setAliasId(1);
        Alias aliasDupe = makeAlias();
        aliasDupe.setAliasId(2);
        List<Alias> existing = new ArrayList<>();

        existing.add(alias);
        when(repository.findByAgent(1)).thenReturn(existing);
        Result<Alias> actual = service.add(aliasDupe);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldAdd() {
        Alias alias = makeAlias();
        Alias mockOut = makeAlias();
        mockOut.setAliasId(1);

        when(repository.add(alias)).thenReturn(mockOut);

        Result<Alias> actual = service.add(alias);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldNotUpdateWhenDoesNotExist() {
        Alias alias = makeAlias();
        Result<Alias> actual = service.update(alias);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldNotUpdateWhenDupeExists() {
        Alias aliasA = makeAlias();
        aliasA.setAliasId(1);
        Alias aliasB = makeAlias();
        aliasB.setAliasId(2);
        aliasB.setName("Bork");
        Alias aliasDupe = makeAlias();
        aliasDupe.setAliasId(2);
        List<Alias> existing = new ArrayList<>();

        existing.add(aliasA);
        existing.add(aliasB);
        when(repository.findByAgent(1)).thenReturn(existing);
        Result<Alias> actual = service.update(aliasDupe);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldUpdate() {
        Alias alias = makeAlias();
        alias.setAliasId(1);

        when(repository.update(alias)).thenReturn(true);

        Result<Alias> actual = service.update(alias);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    Alias makeAlias() {
        Alias alias = new Alias();
        alias.setName("Bond");
        alias.setPersona("Happy");
        alias.setAgentId(1);
        return alias;
    }
}
