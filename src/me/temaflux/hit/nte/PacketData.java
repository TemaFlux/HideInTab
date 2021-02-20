package me.temaflux.hit.nte;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
enum PacketData {

    v1_7("e", "c", "d", "a", "f", "g", "b", "NA", "NA", "NA"),
    cauldron("field_149317_e", "field_149319_c", "field_149316_d", "field_149320_a",
            "field_149314_f", "field_149315_g", "field_149318_b", "NA", "NA", "NA"),
    v1_8("g", "c", "d", "a", "h", "i", "b", "NA", "NA", "e"),
    v1_9("h", "c", "d", "a", "i", "j", "b", "NA", "f", "e"),
    v1_10("h", "c", "d", "a", "i", "j", "b", "NA", "f", "e"),
    v1_11("h", "c", "d", "a", "i", "j", "b", "NA", "f", "e"),
    v1_12("h", "c", "d", "a", "i", "j", "b", "NA", "f", "e"),
    v1_13("h", "c", "d", "a", "i", "j", "b", "g", "f", "e"),
    v1_14("h", "c", "d", "a", "i", "j", "b", "g", "f", "e"),
    v1_15("h", "c", "d", "a", "i", "j", "b", "g", "f", "e"),
    v1_16("h", "c", "d", "a", "i", "j", "b", "g", "f", "e");
    
    public final String members;
    public final String prefix;
    public final String suffix;
    public final String teamName;
    public final String paramInt;
    public final String packOption;
    public final String displayName;
    public final String color;
    public final String push;
    public final String visibility;

    PacketData(String members, String prefix, String suffix, String teamName, String paramInt, String packOption, String displayName, String color, String push, String visibility) {
    	this.members = members;
    	this.prefix = prefix;
    	this.suffix = suffix;
    	this.teamName = teamName;
    	this.paramInt = paramInt;
    	this.packOption = packOption;
    	this.displayName = displayName;
    	this.color = color;
    	this.push = push;
    	this.visibility = visibility;
    }
}