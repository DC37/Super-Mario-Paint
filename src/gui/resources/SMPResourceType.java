package gui.resources;

public enum SMPResourceType {

    UNCATEGORIZED("."),
    STYLE("styles"),
    UI("ui"),
    SOUNDFONT("soundfonts"),
    INSTRUMENT("instruments/normal"),
    INSTRUMENT_GRAY("instruments/gray"),
    INSTRUMENT_SILHOUETTE("instruments/silhouettes");
    
    private String prefix;
    
    SMPResourceType(String prefix) {
        this.prefix = prefix;
    }
    
    public String getPrefix() {
        return prefix;
    }
    
}
