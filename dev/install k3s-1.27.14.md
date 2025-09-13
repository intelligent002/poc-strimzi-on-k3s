# Prerequisites:

* Add secondary drive to vm, with 100gb
* format as ext4 to improve k3s filesystem monitoring
* xfs also works, but complaints
* mount as /data

## install exact dev version
```
curl -sfL https://get.k3s.io | INSTALL_K3S_VERSION="v1.27.14+k3s1" sh -s - --disable traefik --data-dir /data/k3s --default-local-storage-path /data/k3s/pv
```

### or uninstall
```
/usr/local/bin/k3s-uninstall.sh
```

## here is the result kube config
```
cat /etc/rancher/k3s/k3s.yaml
```