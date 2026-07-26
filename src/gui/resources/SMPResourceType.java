package gui.resources;

public enum SMPResourceType {

    UNCATEGORIZED("."),
    STYLE("styles"),
    UI("ui"),
    CURSOR("ui/cursors"),
    SOUNDFONT("soundfonts"),
    INSTRUMENT("instruments/normal"),
    INSTRUMENT_SUSTAINED_OFF("instruments/sustained_off"),
    INSTRUMENT_SUSTAINED_ON("instruments/sustained_on"),
    INSTRUMENT_GRAY("instruments/gray"),
    INSTRUMENT_SILHOUETTE("instruments/silhouettes"),
    ACCIDENTAL("accidentals/normal"),
    ACCIDENTAL_GRAY("accidentals/gray"),
    ACCIDENTAL_SILHOUETTE("accidentals/silhouettes");
    
    private String prefix;
    
    SMPResourceType(String prefix) {
        this.prefix = prefix;
    }
    
    public String getPrefix() {
        return prefix;
    }
    
}
