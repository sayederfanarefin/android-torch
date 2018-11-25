/*
 *     Copyright (C) 2016  Merbin J Anselm <merbinjanselm@gmail.com>
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package info.sayederfanarefin.torch.main.manager.device.output;

import android.content.Context;

import info.sayederfanarefin.anselmbros.torch.main.manager.device.Device;
import info.sayederfanarefin.anselmbros.torch.utils.Constants;
import info.sayederfanarefin.torch.main.manager.device.Device;
import info.sayederfanarefin.torch.utils.Constants;

/**
 * Created by I327891 on 04-Feb-17.
 */

public abstract class OutputDevice extends Device {

    public static final String TYPE = Constants.ID_DEVICE_OUTPUT;

    private boolean mStatus;
    private OutputDeviceListener mListener;

    public OutputDevice(Context context) {
        super(context);
        this.deviceType = TYPE;
        this.mStatus = false;
    }

    protected abstract void turnOn();

    protected abstract void turnOff();

    public final void start(boolean status) {
        if (this.isEnabled) {
            if (status && !this.mStatus)
                this.turnOn();
            else if (!status && this.mStatus)
                this.turnOff();
        }
    }

    public final void toggle() {
        this.start(!this.mStatus);
    }

    public final boolean getStatus() {
        return this.mStatus;
    }

    public final void setListener(OutputDeviceListener listener) {
        this.mListener = listener;
    }

    protected final void updateStatus(boolean status) {
        this.mStatus = status;
        if (this.mListener != null) {
            this.mListener.onStatusChanged(this.deviceType, status);
        }
    }

    protected final void updateError(String error) {
        if (this.mListener != null) {
            this.mListener.onError(error);
        }
    }
}
