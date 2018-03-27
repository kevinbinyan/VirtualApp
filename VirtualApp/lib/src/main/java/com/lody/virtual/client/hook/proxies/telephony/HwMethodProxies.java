package com.lody.virtual.client.hook.proxies.telephony;

import android.os.Bundle;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import com.lody.virtual.client.hook.base.ReplaceCallingPkgMethodProxy;
import com.lody.virtual.client.hook.base.StaticMethodProxy;
import com.lody.virtual.client.ipc.VirtualLocationManager;
import com.lody.virtual.helper.ParamSettings;
import com.lody.virtual.helper.utils.marks.FakeDeviceMark;
import com.lody.virtual.helper.utils.marks.FakeLocMark;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.remote.vloc.VCell;

import java.lang.reflect.Method;

/**
 * @author Lody
 */
@SuppressWarnings("ALL")
class HwMethodProxies {

    @FakeDeviceMark("fake device id.")
    static class GetUniqueDeviceId extends StaticMethodProxy {

        public GetUniqueDeviceId() {
            super("getUniqueDeviceId");
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
//            return getDeviceInfo().deviceId;
            return ParamSettings.getDeviceIds()[VUserHandle.myUserId()];
        }
    }

    @FakeLocMark("cell location")
    static class GetCellLocation extends ReplaceCallingPkgMethodProxy {

        public GetCellLocation() {
            super("getCellLocation");
        }

        @Override
        public Object call(Object who, Method method, Object... args) throws Throwable {
            if (isFakeLocationEnable()) {
                VCell cell = VirtualLocationManager.get().getCell(getAppUserId(), getAppPkg());
                if (cell != null) {
                    return getCellLocationInternal(cell);
                }
            }
            return super.call(who, method, args);
        }
    }

    private static Bundle getCellLocationInternal(VCell cell) {
        if (cell != null) {
            Bundle cellData = new Bundle();
            if (cell.type == 2) {
                try {
                    CdmaCellLocation cellLoc = new CdmaCellLocation();
                    cellLoc.setCellLocationData(cell.baseStationId, Integer.MAX_VALUE, Integer.MAX_VALUE, cell.systemId, cell.networkId);
                    cellLoc.fillInNotifierBundle(cellData);
                } catch (Throwable e) {
                    cellData.putInt("baseStationId", cell.baseStationId);
                    cellData.putInt("baseStationLatitude", Integer.MAX_VALUE);
                    cellData.putInt("baseStationLongitude", Integer.MAX_VALUE);
                    cellData.putInt("systemId", cell.systemId);
                    cellData.putInt("networkId", cell.networkId);
                }
            } else {
                try {
                    GsmCellLocation cellLoc = new GsmCellLocation();
                    cellLoc.setLacAndCid(cell.lac, cell.cid);
                    cellLoc.fillInNotifierBundle(cellData);
                } catch (Throwable e) {
                    cellData.putInt("lac", cell.lac);
                    cellData.putInt("cid", cell.cid);
                    cellData.putInt("psc", cell.psc);
                }
            }
            return cellData;
        }
        return null;
    }


    private static CellInfo createCellInfo(VCell cell) {
        if (cell.type == 2) { // CDMA
            CellInfoCdma cdma = mirror.android.telephony.CellInfoCdma.ctor.newInstance();
            CellIdentityCdma identityCdma = mirror.android.telephony.CellInfoCdma.mCellIdentityCdma.get(cdma);
            CellSignalStrengthCdma strengthCdma = mirror.android.telephony.CellInfoCdma.mCellSignalStrengthCdma.get(cdma);
            mirror.android.telephony.CellIdentityCdma.mNetworkId.set(identityCdma, cell.networkId);
            mirror.android.telephony.CellIdentityCdma.mSystemId.set(identityCdma, cell.systemId);
            mirror.android.telephony.CellIdentityCdma.mBasestationId.set(identityCdma, cell.baseStationId);
            mirror.android.telephony.CellSignalStrengthCdma.mCdmaDbm.set(strengthCdma, -74);
            mirror.android.telephony.CellSignalStrengthCdma.mCdmaEcio.set(strengthCdma, -91);
            mirror.android.telephony.CellSignalStrengthCdma.mEvdoDbm.set(strengthCdma, -64);
            mirror.android.telephony.CellSignalStrengthCdma.mEvdoSnr.set(strengthCdma, 7);
            return cdma;
        } else { // GSM
            CellInfoGsm gsm = mirror.android.telephony.CellInfoGsm.ctor.newInstance();
            CellIdentityGsm identityGsm = mirror.android.telephony.CellInfoGsm.mCellIdentityGsm.get(gsm);
            CellSignalStrengthGsm strengthGsm = mirror.android.telephony.CellInfoGsm.mCellSignalStrengthGsm.get(gsm);
            mirror.android.telephony.CellIdentityGsm.mMcc.set(identityGsm, cell.mcc);
            mirror.android.telephony.CellIdentityGsm.mMnc.set(identityGsm, cell.mnc);
            mirror.android.telephony.CellIdentityGsm.mLac.set(identityGsm, cell.lac);
            mirror.android.telephony.CellIdentityGsm.mCid.set(identityGsm, cell.cid);
            mirror.android.telephony.CellSignalStrengthGsm.mSignalStrength.set(strengthGsm, 20);
            mirror.android.telephony.CellSignalStrengthGsm.mBitErrorRate.set(strengthGsm, 0);
            return gsm;
        }
    }


}