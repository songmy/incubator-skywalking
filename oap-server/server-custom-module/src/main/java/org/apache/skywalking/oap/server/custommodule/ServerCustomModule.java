package org.apache.skywalking.oap.server.custommodule;

import org.apache.skywalking.oap.server.library.module.ModuleDefine;

/**
 * @author songmy
 */
public class ServerCustomModule extends ModuleDefine {

    public static final String NAME = "ServerCustomModule";

    public ServerCustomModule() {
        super(NAME);
    }

    @Override
    public Class[] services() {
        return new Class[0];
    }
}
