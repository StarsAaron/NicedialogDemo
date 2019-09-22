package com.aaron.nicedialoglibrary;

import java.io.Serializable;

public interface ViewConvertListener extends Serializable {
    long serialVersionUID = System.currentTimeMillis();

    void convertView(ViewHolder holder, NiceDialog dialog);
}
