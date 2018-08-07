package martinbradley.hospital.web.beans;

@SexTranslator
public class SexToBoolean
{
    public boolean isMaleFromString(String sex)
    {
        return sex.equalsIgnoreCase("M");
    }

    public String toStringFromMale(boolean sex)
    {
        return sex ? "M" : "F";
    }
}
