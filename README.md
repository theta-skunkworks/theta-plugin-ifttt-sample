# RICOH THETA IFTTT Sample

撮影ボタンを押すと IFTTT の Webhoook URL にリクエストを送るサンプルプラグインです。

## 準備
* Webhook をトリガーとする IFTTT アプレットを作成する
* IFTTT の Webhook キーと Webhook イベント名を設定する

```java
private static final String WEBHOOK_KEY = "write ifttt webhook key here";
private static final String WEBHOOK_EVENT_NAME = "write ifttt webhook event name here";
```


## 操作方法

| 操作                      | 機能                              |
| ------------------------- | --------------------------------- |
| シャッターボタン          | 指定URLにPOSTリクエストを送信する (CLモードで動作) |
| モードボタン (長押し)     | プラグイン起動・終了              |

