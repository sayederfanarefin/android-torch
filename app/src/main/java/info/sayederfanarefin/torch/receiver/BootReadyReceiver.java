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

package info.sayederfanarefin.torch.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import info.sayederfanarefin.anselmbros.torch.R;
import info.sayederfanarefin.anselmbros.torch.service.TorchieQuick;
import info.sayederfanarefin.anselmbros.torch.utils.NotificationUtils;
import info.sayederfanarefin.torch.service.TorchieQuick;

/**
 * Created by anselm94 on 21/4/16.
 */
public class BootReadyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            if (TorchieQuick.getInstance() == null) {
                NotificationUtils.sendNotification(context, String.format(context.getResources().getString(R.string.notify_title)), String.format(context.getResources().getString(R.string.notify_text)));
            }
        }
    }
}
