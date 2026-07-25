package gui.resources;

public enum SMPResourceType {

    UNCATEGORIZED("."),
    STYLE("styles"),
    UI("ui"),
    SOUNDFONT("soundfonts"),
    INSTRUMENT("instruments/normal");
    
    private String prefix;
    
    SMPResourceType(String prefix) {
        this.prefix = prefix;
    }
    
    public String getPrefix() {
        return prefix;
    }
    
}
