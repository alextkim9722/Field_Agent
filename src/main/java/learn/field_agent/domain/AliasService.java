package learn.field_agent.domain;

import learn.field_agent.data.AliasRepository;
import learn.field_agent.models.Alias;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AliasService {

    private final AliasRepository repository;

    public AliasService(AliasRepository repository) {
        this.repository = repository;
    }

    public Result<Alias> add(Alias alias) {
        Result<Alias> result = validate(alias);
        if (!result.isSuccess()) {
            return result;
        }

        if (alias.getAliasId() != 0) {
            result.addMessage("aliasId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        alias = repository.add(alias);
        result.setPayload(alias);
        return result;
    }

    public Result<Alias> update(Alias alias) {
        Result<Alias> result = validate(alias);
        if (!result.isSuccess()) {
            return result;
        }

        if (alias.getAliasId() <= 0) {
            result.addMessage("aliasId must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if (!repository.update(alias)) {
            String msg = String.format("aliasId: %s, not found", alias.getAliasId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int aliasId) {
        return repository.deleteById(aliasId);
    }

    private Result<Alias> validate(Alias alias) {
        Result<Alias> result = new Result<>();
        List<Alias> aliases = repository.findByAgent(alias.getAgentId());

        if (alias == null) {
            result.addMessage("alias cannot be null", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(alias.getName())) {
            result.addMessage("name is required", ResultType.INVALID);
        }

        if (aliases != null){
            for (Alias currentAlias : aliases) {
                if(currentAlias.getName().equalsIgnoreCase(alias.getName())
                && Validations.isNullOrBlank(currentAlias.getPersona())
                && Validations.isNullOrBlank(alias.getPersona()))
                {
                    result.addMessage("Alias requires a persona", ResultType.INVALID);
                }
                if(currentAlias.getPersona() != null && alias.getPersona() != null) {
                    if (currentAlias.getName().equalsIgnoreCase(alias.getName())
                            && currentAlias.getPersona().equalsIgnoreCase(alias.getPersona())) {
                        result.addMessage("Alias has a duplicate", ResultType.INVALID);
                    }
                }
            }
        }

        return result;
    }
}
