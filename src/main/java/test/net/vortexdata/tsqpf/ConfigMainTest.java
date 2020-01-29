package test.net.vortexdata.tsqpf;

import net.vortexdata.tsqpf.configs.ConfigMain;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigMainTest {

    /**
     * <p>getNonExistentConfigValue.</p>
     */
    @Test
    public void getNonExistentConfigValue() {
        System.out.println("getNonExistentConfigValue");
        ConfigMain configMain = new ConfigMain();
        configMain.load();
        assertEquals("", configMain.getProperty("someKeyThatDoesNotExist"));
    }

    /**
     * <p>getValue.</p>
     */
    @Test
    public void getValue() {
        System.out.println("getValue");
        ConfigMain configMain = new ConfigMain();
        assertEquals("serveradmin", configMain.getProperty("queryUser"));
    }

    /**
     * <p>getDefaultValue.</p>
     */
    @Test
    public void getDefaultValue() {
        System.out.println("getDefaultValue");
        ConfigMain configMain = new ConfigMain();
        assertEquals("1", configMain.getDefaultProperty("virtualServerId"));
    }

}
