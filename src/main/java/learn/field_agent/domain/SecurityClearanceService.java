package learn.field_agent.domain;

import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.SecurityClearance;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityClearanceService {
    private final SecurityClearanceRepository repository;

    public SecurityClearanceService(SecurityClearanceRepository repository){ this.repository = repository; }

    public List<SecurityClearance> findAll(){ return repository.findAll(); }

    public Result<SecurityClearance> findById(int securityId) {
        Result<SecurityClearance> result = new Result<>();
        SecurityClearance securityClearance = repository.findById(securityId);
        if(securityClearance == null) {
            result.addMessage("Security Clearance is not found.", ResultType.NOT_FOUND);
            return result;
        }

        result.setPayload(securityClearance);
        return result;
    }

    public Result<SecurityClearance> add(SecurityClearance securityClearance) {
        Result<SecurityClearance> result = validate(securityClearance);
        if (!result.isSuccess()) {
            return result;
        }

        if (securityClearance.getSecurityClearanceId() != 0) {
            result.addMessage("securityId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        securityClearance = repository.add(securityClearance);
        result.setPayload(securityClearance);
        return result;
    }

    public Result<SecurityClearance> update(SecurityClearance securityClearance) {
        Result<SecurityClearance> result = validate(securityClearance);
        if (!result.isSuccess()) {
            return result;
        }

        if (securityClearance.getSecurityClearanceId() <= 0) {
            result.addMessage("securityId must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if (!repository.update(securityClearance)) {
            String msg = String.format("securityId: %s, not found", securityClearance.getSecurityClearanceId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public Result<SecurityClearance> deleteById(int securityId) {
        Result<SecurityClearance> result = new Result<>();

        if(repository.getConnections(securityId) > 0) {
            String msg = String.format("securityId: %s, has several children", securityId);
            result.addMessage(msg, ResultType.INVALID);
            return result;
        }

        if(!repository.deleteById(securityId)) {
            String msg = String.format("securityId: %s, does not exist", securityId);
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    private Result<SecurityClearance> validate(SecurityClearance securityClearance) {
        Result<SecurityClearance> result = new Result<>();
        List<SecurityClearance> securityClearanceList = findAll();

        if (securityClearance == null) {
            result.addMessage("security cannot be null", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(securityClearance.getName())) {
            result.addMessage("clearance name is required", ResultType.INVALID);
        }

        for(SecurityClearance currentClearance : securityClearanceList) {
            if(securityClearance.getName().equalsIgnoreCase(currentClearance.getName())) {
                result.addMessage("clearance name has a duplicate", ResultType.INVALID);
            }
        }

        return result;
    }
}
