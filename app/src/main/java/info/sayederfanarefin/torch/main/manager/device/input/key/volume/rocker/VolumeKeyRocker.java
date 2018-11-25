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

package info.sayederfanarefin.torch.main.manager.device.input.key.volume.rocker;

import android.content.Context;
import android.view.InputEvent;

import info.sayederfanarefin.torch.main.manager.device.input.event.VolumeKeyEvent;
import info.sayederfanarefin.torch.main.manager.device.input.key.volume.VolumeKeyDevice;
import info.sayederfanarefin.torch.utils.Constants;
import info.sayederfanarefin.torch.main.manager.device.input.event.VolumeKeyEvent;
import info.sayederfanarefin.torch.main.manager.device.input.key.volume.VolumeKeyDevice;

/**
 * Created by I327891 on 04-Feb-17.
 */

public class VolumeKeyRocker extends VolumeKeyDevice {
    public static final String TYPE = Constants.ID_DEVICE_INPUT_VOLUMEKEY_ROCKER;

    private final static int MAX_BUFFER_SIZE = 20;
    private int[] buffer;
    private int current_ptr;

    public VolumeKeyRocker(Context context) {
        super(context);
        this.deviceType = TYPE;
        buffer = new int[MAX_BUFFER_SIZE];
    }

    @Override
    public boolean setEvent(InputEvent event) {
        VolumeKeyEvent volumeKeyEvent = (VolumeKeyEvent) event;
        if (volumeKeyEvent.getVolumeKeyEventType() == VolumeKeyEvent.VOLUME_KEY_EVENT_ROCKER && volumeKeyEvent.isVolumeKeyEvent()) {
            buffer[current_ptr] = volumeKeyEvent.getPrevCurrentValue()[0];
            current_ptr++;
            buffer[current_ptr] = volumeKeyEvent.getPrevCurrentValue()[1];
            current_ptr++;
            if (current_ptr == MAX_BUFFER_SIZE) {
                current_ptr = 0;
            }
            return this.isActionModePerformed();
        }
        return false;
    }

    @Override
    protected boolean isKeyComboPerformed() {
        boolean keyComboPerformed = false;
        for (int i = 0; i < current_ptr; i = i + 2) {
            if (buffer[i] != buffer[i + 1]) {
                if (i < (MAX_BUFFER_SIZE - 2)) {
                    if ((buffer[i] == buffer[i + 3]) && (buffer[i + 1] == buffer[i + 2])) {
                        this.clearBuffer();
                        keyComboPerformed = true;
                        break;
                    }
                } else {
                    if ((buffer[MAX_BUFFER_SIZE - 2] == buffer[1]) && (buffer[MAX_BUFFER_SIZE - 1] == buffer[0])) {
                        this.clearBuffer();
                        keyComboPerformed = true;
                        break;
                    }
                }
            }
        }
        if (keyComboPerformed) {
            this.updateCurrentSignal(INP_TRIGGER);
        }
        return keyComboPerformed;
    }

    private void clearBuffer() {
        buffer = new int[MAX_BUFFER_SIZE];
        current_ptr = 0;
    }
}
