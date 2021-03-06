#!/system/bin/sh

config=$1
path=/system/vendor/bin/perfd

if [[ ! -f $path ]]; then
    if [[ ! -f $path.bak ]]; then
        echo '当前设备或系统不支持此操作！' >&2
        exit 0;
    fi;
fi;

echo 'Step1.挂载System、Vendor为读写...'

$BUSYBOX mount -o rw,remount /system
mount -o rw,remount /system
$BUSYBOX mount -o remount,rw /dev/block/bootdevice/by-name/system /system
mount -o remount,rw /dev/block/bootdevice/by-name/system /system 2> /dev/null

$BUSYBOX mount -o rw,remount /vendor 2> /dev/null
mount -o rw,remount /vendor 2> /dev/null
$BUSYBOX mount -o rw,remount /system/vendor 2> /dev/null
mount -o rw,remount /system/vendor 2> /dev/null

if [[ -e /dev/block/bootdevice/by-name/vendor ]]; then
    $BUSYBOX mount -o rw,remount /dev/block/bootdevice/by-name/vendor /vendor 2> /dev/null
    mount -o rw,remount /dev/block/bootdevice/by-name/vendor /vendor 2> /dev/null
fi

if [[ "$config" = "1" ]]; then
    if [[ -f "$path.bak" ]]; then
        mv "$path.bak" "$path"
        chmod 0755 "$path"
        echo "Step2.还原（${path}.bak）->（$path） ..."
        if [[ -f "$path" ]]
        then
            echo '操作完成...'
            echo '现在，请重启手机使修改生效！'
        else
            echo '操作失败，System未解锁或文件被锁定...' >&2
        fi
    else
        echo '备份文件不存在，无法还原...' >&2
    fi;
elif [[ "$config" = "0" ]]; then
    if [[ -f "$path" ]] && [[ ! -f "$path.bak" ]]; then
        mv "$path" "$path.bak"
        chmod 0755 "$path.bak"
        echo "Step2.重命名 （$path） ->（${path}.bak） ..."
        if [[ -f "$path" ]]
        then
            echo '操作失败，System未解锁或文件被锁定...' >&2
        else
            echo '操作完成...'
            echo '现在，请重启手机使修改生效！'
        fi
    elif [[ ! -f "$path" ]]; then
        echo '原文件已被移除，无需再重复操作...' >&2
    fi;
    rm -f "$path" 2> /dev/null
fi;
