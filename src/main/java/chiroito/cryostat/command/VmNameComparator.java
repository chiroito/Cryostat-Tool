package chiroito.cryostat.command;

import chiroito.cryostat.api.VMInfo;

import java.util.Comparator;

public class VmNameComparator implements Comparator<VMInfo> {
    @Override
    public int compare(VMInfo o1, VMInfo o2) {
        return o1.alias.compareTo(o2.alias);
    }
}
