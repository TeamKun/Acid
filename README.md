# Acid
水に触れるとダメージを受けるプラグイン<br>

* 開始するとあらゆる場所で雨が降る
* デフォルトでは以下の条件を満たすとダメージを受ける
  * 雨に当たる
  * 水源・水流に触れる
  * ポーションを使用する
  * 投げられたポーションに当たる
* 以下はコマンドから設定可能
  * ポーションのエフェクトがかかった時にダメージを受けるようにする
  * Mobもダメージを受けるようにする

## 動作環境
- Minecraft 1.16.5
- PaperMC 1.16.5

## コマンド

- sh
    - start

      - 開始

    - stop

      - 終了

    - reloadConfig

      - 設定をリロードする

    - setConfig <設定名> <値>

      - <設定名>に<値>を設定する
      - 設定可能な設定名と値は以降を参照

    - showStatus

      各種設定の設定値を出力
      

## setConfigで設定可能な設定名と値

| 設定名                | 内容                                           | 設定可能な値 | デフォルト値 | 関係する事件名                                               |
| --------------------- | ---------------------------------------------- | ------------ | ------------ | ------------------------------------------------------------ |
| happeningSwitchTime   | 事件が起きるまでの秒数                         | 0以上の整数  | 180          | -                                                            |
| nextHappeningShowTime | 次の事件発生前に起きる事件が表示される秒数     | 0以上の整数  | 10           | -                                                            |
| spawnMobNum           | Mob発生系の事件で発生するMobの数               | 0以上の整数  | 10           | - spawnPassiveMob<br />- spawnZombie<br />- spawnCleeper<br />- spawnEnderman<br />- spawnBat |
| fallBlockNum          | fall系の事件で発生する落下物の数               | 0以上の整数  | 3            | - fallAnvil<br/>- fallSand<br/>- fallArrow                   |
| teleportRange         | テレポートする事件での移動するテレポートの距離 | 0以上の整数  | 500          | - teleportPlayer                                             |
| convertBlockRange     | ブロック変更を行う事件での変更範囲             | 0以上の整数  | 4            | - convertBlockToStone<br/>- beSpongeBody                     |
| offHappening          | 発生させない事件の指定                         | 事件名       | なし         | なし                                                         |
| onHappening           | 発生させる事件の指定                           | 事件名       | なし         | なし                                                         |
| addWomanPlayer        | 女の命名リストへの追加                         | プレイヤー名 | なし         | - prohibitMan<br/>- prohibitWoman                            |
| removeWomanPlayer     | 女の命名リストからの削除                       | プレイヤー名 | なし         | - prohibitMan<br/>- prohibitWoman                            |
| addNonbinaryPlayer    | 性別振り分けなしのリストへの追加               | プレイヤー名 | なし         | - prohibitMan<br/>- prohibitWoman                            |
| removeNonbinaryPlayer | 性別振り分けなしのリストからの削除             | プレイヤー名 | なし         | - prohibitMan<br/>- prohibitWoman                            |

## setNextHappeningで設定可能な事件名と事件内容

| 事件名 | 事件内容 | 備考 |
| ------ | -------- | -------- |
|spawnPassiveMob|豚、牛、鶏、羊、馬がプレイヤーの周囲にスポーンする|spawnMobNumの数だけスポーンする|
|spawnZombie|ゾンビがプレイヤーの周囲にスポーンする|spawnMobNumの数だけスポーンする|
|spawnCleeper|クリーパーがプレイヤーの周囲にスポーンする|spawnMobNumの数だけスポーンする|
|spawnEnderman|エンダーマン がプレイヤーの周囲にスポーンする|spawnMobNumの数だけスポーンする|
|spawnBat|こうもりがプレイヤーの周囲にスポーンする|spawnMobNumの数だけスポーンする|
|fallAnvil|金床がプレイヤーの周囲に降る|fallAnvilの数だけ降る|
|fallSand|砂ブロックがプレイヤーの周囲に降る|fallAnvilの数だけ降る|
|fallArrow|矢がプレイヤーの周囲に降る|fallAnvilの数だけ降る|
|fallMeteor|ガストの火の玉がプレイヤーに向かって降る||
|changeFootBlockRandom|プレイヤーの足元のブロックがランダムに変わる（ただしポータルなど一部除く）||
|changeFootBlockMagma| プレイヤーの足元のブロックがマグマブロックに変わる           |                                                              |
|changeFootBlockTNT|プレイヤーの足元のブロックがTNTに変わる||
|addPlayerInvisible|プレイヤー・Mobに透明化を付与する||
|addPlayerLevitation|プレイヤーに浮遊を付与する||
|addPlayerBlindness|プレイヤーに盲目を付与する||
|addPlayerRandomPotion|プレイヤーにランダムにポーションの効果を付与する||
|addPlayerMoveSpeedUp|プレイヤーに移動速度増加を付与する||
|addPlayerMiningSpeedUp|プレイヤーに採掘速度増加を付与する||
|addPlayerJumpPowerUp|プレイヤーにジャンプ力増加を付与する||
|addPlayerPoison|プレイヤーに毒を付与する||
|upgradeTool|プレイヤーが所持するツールをアップグレードする||
|downgradeTool|プレイヤーが所持するツールをダウングレードする||
|clearHotBarItem|プレイヤーのホットバーのアイテムを消す||
|transHotBarItem|プレイヤーのホットバーのアイテムを鉄装備一式に置き換える||
|teleportPlayer|プレイヤーをランダムな位置にテレポートする|X,Z軸 ±teleportRangeの範囲でテレポートする|
|changePlayerDimension|プレイヤーのディメンションを移動する|オーバーワールド ->ネザー <br />ネザー -> オーバーワールド <br />エンド -> オーバーワールド|
|dieOnFootBlock|プレイヤーが草、土、ネザー ブロックの上に立つと死亡する||
|beOneHp|プレイヤーの体力が1になる||
|syncDeathEveryone|プレイヤーが死亡すると別のプレイヤーが1人死亡する||
|syncDeathRandom|プレイヤーが死亡すると全プレイヤーが死亡する||
|beVeagan|肉、魚、卵を含む食糧を食べるとダメージを受け、満腹度が減る||
|beCarnivore|肉、魚、卵を除く食糧を食べるとダメージを受け、満腹度が減る||
|prohibitWater|プレイヤーが水源・水流に入ると死亡する||
|prohibitJump|プレイヤーがジャンプすると死亡する||
|convertBlockToStone|プレイヤーの周囲のブロックが石ブロックになる|convertBlockRangeの範囲だけ変換される|
|beSpongeBody|体がスポンジ化する（プレイヤーの周囲の水、マグマを空気に変換する）|convertBlockRangeの範囲だけ変換される|
|prohibitMan|女が男に近づくと燃える|https://github.com/TeamKun/SomethingHappen/blob/master/src/main/resources/config.yml のwomanPlayerおよびnonbinaryPlayerを除くプレイヤーを男として定義<br />womanPlayerを女として定義|
|prohibitWoman|男が女に近づくと燃える|https://github.com/TeamKun/SomethingHappen/blob/master/src/main/resources/config.yml のwomanPlayerおよびnonbinaryPlayerを除くプレイヤーを男として定義<br />womanPlayerを女として定義|
|changeAdventureMode|アドベンチャーモードになる||
