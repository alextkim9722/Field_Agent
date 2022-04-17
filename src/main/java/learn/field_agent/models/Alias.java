package learn.field_agent.models;

public class Alias {
    private int aliasId;
    private String name;
    private String persona;
    private int agentId;

    public Alias() {}

    public Alias(int aliasId, String name, String persona, int agentId) {
        this.aliasId = aliasId;
        this.name = name;
        this.persona = persona;
        this.agentId = agentId;
    }

    public int getAliasId() {
        return aliasId;
    }

    public void setAliasId(int aliasId) {
        this.aliasId = aliasId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public int getAgentId() { return agentId; }

    public void setAgentId(int agentId) { this.agentId = agentId; }
}
