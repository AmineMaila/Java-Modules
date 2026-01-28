package fr.forty_two.printer.app;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.ParameterException;
import com.diogonunes.jcolor.Attribute;
import com.beust.jcommander.IStringConverter;

class AttributeConverter implements IStringConverter<Attribute> {
    @Override
    public Attribute convert(String value) {
        return switch(value) {
            case "BLACK" -> Attribute.BLACK_BACK();
            case "RED" -> Attribute.RED_BACK();
            case "YELLOW" -> Attribute.YELLOW_BACK();
            case "GREEN" -> Attribute.GREEN_BACK();
            case "BLUE" -> Attribute.BLUE_BACK();
            case "MAGENTA" -> Attribute.MAGENTA_BACK();
            case "CYAN" -> Attribute.CYAN_BACK();
            case "WHITE" -> Attribute.WHITE_BACK();
            default -> throw new ParameterException("Unsupported Color");
        };
    }
}

@Parameters(separators = "=")
public class Args {
    @Parameter(
        names = {"--white", "-w"},
        description = "color for white pixel",
        required = true,
        converter = AttributeConverter.class
    )
    private Attribute white;

    public Attribute getWhite() {
        return white; 
    }

    @Parameter(
        names = {"--black", "-b"},
        description = "color for black pixel",
        required = true,
        converter = AttributeConverter.class
    )
    private Attribute black;

    public Attribute getBlack() {
        return black;
    }
}
