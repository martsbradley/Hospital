package martinbradley.hospital.rest;

import javax.ws.rs.core.Application;
import javax.ws.rs.ApplicationPath;
import java.util.HashSet;
import java.util.Set;


@ApplicationPath("/rest")
public class HospitalApplication extends Application
{
    private Set<Class<?>> empty = new HashSet<Class<?>>();
    private Set<Object> singletons = new HashSet<>();

    public HospitalApplication()
    {
        singletons.add(new HospitalResourceImpl());
    }

    public Set<Class<?>> getClasses()
    {
        return empty;
    }

    public Set<Object> getSingletons()
    {
        return singletons;
    }
}
