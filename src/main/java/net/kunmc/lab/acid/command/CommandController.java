package net.kunmc.lab.acid.command;

import net.kunmc.lab.acid.Config;
import net.kunmc.lab.acid.game.GameManager;
import net.kunmc.lab.acid.util.Const;
import net.kunmc.lab.acid.util.DecolationConst;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandController implements CommandExecutor, TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(Stream.of(
                    Const.START,
                    Const.STOP,
                    Const.RELOAD_CONFIG,
                    Const.SET_CONFIG,
                    Const.SHOW_STATUS)
                    .filter(e -> e.startsWith(args[0])).collect(Collectors.toList()));
        } else if (args.length == 3 && args[0].equals(Const.SET_CONFIG) &&
                ((args[1].equals(Const.DAMAGE) ||
                        args[1].equals(Const.SPLASH_DAMAGE) ||
                        args[1].equals(Const.DAMAGE_TICK) ||
                        args[1].equals(Const.RAIN_NUM) ||
                        args[1].equals(Const.RAIN_POINT)))) {
            completions.add("<数字>");
        } else if (args.length == 2 && args[0].equals(Const.SET_CONFIG)) {
            completions.addAll(Stream.of(
                    Const.DAMAGE_TICK,
                    Const.DAMAGE,
                    Const.SPLASH_DAMAGE,
                    Const.RAIN_NUM,
                    Const.RAIN_POINT,
                    Const.OFF_ACID_TARGET,
                    Const.ON_ACID_TARGET)
                    .filter(e -> e.startsWith(args[1])).collect(Collectors.toList()));
        } else if (args.length == 3 && args[1].equals(Const.ON_ACID_TARGET)) {
            List<String> tmpCompletion = new ArrayList<>();
            for (Map.Entry<String, Boolean> switchTarget : Config.booleanConf.entrySet()) {
                if (!switchTarget.getValue()) {
                    tmpCompletion.add(switchTarget.getKey());
                }
            }
            completions.addAll(tmpCompletion.stream().filter(e -> e.startsWith(args[2])).collect(Collectors.toList()));
        } else if (args.length == 3 && args[1].equals(Const.OFF_ACID_TARGET)) {
            List<String> tmpCompletion = new ArrayList<>();
            for (Map.Entry<String, Boolean> switchTarget : Config.booleanConf.entrySet()) {
                if (switchTarget.getValue()) {
                    tmpCompletion.add(switchTarget.getKey());
                }
            }
            completions.addAll(tmpCompletion.stream().filter(e -> e.startsWith(args[2])).collect(Collectors.toList()));
        }
        return completions;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendUsage(sender);
            return true;
        }

        String commandName = args[0];
        switch (commandName) {
            case Const.START:
                if (GameManager.runningMode == GameManager.GameMode.RUNNING) {
                    sender.sendMessage(DecolationConst.RED + "すでに開始しています");
                    return true;
                }
                if (!checkArgsNum(sender, args.length, 1)) return true;
                GameManager.controller(GameManager.GameMode.RUNNING);
                sender.sendMessage(DecolationConst.GREEN + "開始します");
                break;
            case Const.STOP:
                if (GameManager.runningMode == GameManager.GameMode.NEUTRAL) {
                    sender.sendMessage(DecolationConst.RED + "開始されていません");
                    return true;
                }
                if (args.length != 1) {
                    sender.sendMessage(DecolationConst.RED + "引数が不要なコマンドです");
                    return true;
                }
                GameManager.controller(GameManager.GameMode.NEUTRAL);
                sender.sendMessage(DecolationConst.GREEN + "終了します");
                break;
            case Const.RELOAD_CONFIG:
                if (args.length != 1) {
                    sender.sendMessage(DecolationConst.RED + "引数が不要なコマンドです");
                    return true;
                }
                Config.loadConfig(true);
                sender.sendMessage(DecolationConst.GREEN + "設定をリロードしました");
                break;
            case Const.SET_CONFIG:
                switch (args[1]) {
                    case Const.DAMAGE:
                        setIntConfig(sender, args, 3, Const.DAMAGE);
                        break;
                    case Const.SPLASH_DAMAGE:
                        setIntConfig(sender, args, 3, Const.SPLASH_DAMAGE);
                        break;
                    case Const.RAIN_NUM:
                        setIntConfig(sender, args, 3, Const.RAIN_NUM);
                        break;
                    case Const.RAIN_POINT:
                        setIntConfig(sender, args, 3, Const.RAIN_POINT);
                        break;
                    case Const.DAMAGE_TICK:
                        setIntConfig(sender, args, 3, Const.DAMAGE_TICK);
                        break;
                    case Const.ON_ACID_TARGET:
                        if (!checkArgsNum(sender, args.length, 3)) return true;
                        if (!Config.booleanConf.containsKey(args[2]))
                            sender.sendMessage(DecolationConst.RED + "存在しない設定です");
                        if (Config.booleanConf.get(args[2])) sender.sendMessage(DecolationConst.AQUA + "すでにONになっています");
                        Config.booleanConf.put(args[2], true);
                        sender.sendMessage(DecolationConst.GREEN + args[2] + "の設定をONにしました");
                        Config.saveConfig(args[2]);
                        break;
                    case Const.OFF_ACID_TARGET:
                        if (!checkArgsNum(sender, args.length, 3)) return true;
                        if (!Config.booleanConf.containsKey(args[2]))
                            sender.sendMessage(DecolationConst.RED + "存在しない設定です");
                        if (!Config.booleanConf.get(args[2]))
                            sender.sendMessage(DecolationConst.AQUA + "すでにOFFになっています");
                        Config.booleanConf.put(args[2], false);
                        sender.sendMessage(DecolationConst.GREEN + args[2] + "の設定をOFFにしました");
                        Config.saveConfig(args[2]);
                        break;
                    default:
                        sender.sendMessage(DecolationConst.RED + "存在しない設定項目です");
                        sendUsage(sender);
                }
                break;
            case Const.SHOW_STATUS:
                if (args.length != 1) {
                    sender.sendMessage(DecolationConst.RED + "引数が不要なコマンドです");
                    return true;
                }
                sender.sendMessage(DecolationConst.GREEN + "設定値一覧");
                List<String> acidSwitch = new ArrayList<>();
                for (Map.Entry<String, Boolean> acidTarget : Config.booleanConf.entrySet()) {
                    System.out.println(acidTarget);
                    if (acidTarget.getValue()) acidSwitch.add(acidTarget.getKey());
                }
                String prefix = "  ";
                for (Map.Entry<String, Integer> param : Config.intConf.entrySet()) {
                    sender.sendMessage(String.format("%s%s: %s", prefix, param.getKey(), param.getValue()));
                }
                sender.sendMessage(String.format("%sacidSwitch: ", prefix) + acidSwitch);
                break;
            default:
                sender.sendMessage(DecolationConst.RED + "存在しないコマンドです");
                sendUsage(sender);
        }
        return true;
    }

    private void sendUsage(CommandSender sender) {
        String usagePrefix = String.format("  /%s ", Const.MAIN);
        String descPrefix = "  ";
        sender.sendMessage(DecolationConst.GREEN + "Usage:");
        sender.sendMessage(String.format("%s%s"
                , usagePrefix, Const.START));
        sender.sendMessage(String.format("%s開始", descPrefix));
        sender.sendMessage(String.format("%s%s"
                , usagePrefix, Const.STOP));
        sender.sendMessage(String.format("%s終了", descPrefix));
        sender.sendMessage(String.format("%s%s %s <number>"
                , usagePrefix, Const.SET_CONFIG, Const.DAMAGE));
        sender.sendMessage(String.format("%s水や雨で濡れている時に受けるダメージ", descPrefix));
        sender.sendMessage(String.format("%s%s %s <number>"
                , usagePrefix, Const.SET_CONFIG, Const.SPLASH_DAMAGE));
        sender.sendMessage(String.format("%sポーションなどを浴びた時に受けるダメージ", descPrefix));
        sender.sendMessage(String.format("%s%s %s <number>"
                , usagePrefix, Const.SET_CONFIG, Const.DAMAGE_TICK));
        sender.sendMessage(String.format("%s水や雨で濡れている時にダメージを受ける間隔(Tick)", descPrefix));
        sender.sendMessage(String.format("%s%s %s <number>"
                , usagePrefix, Const.SET_CONFIG, Const.RAIN_NUM));
        sender.sendMessage(String.format("%s1ティックあたりに発生する雨パーティションの数", descPrefix));
        sender.sendMessage(String.format("%s%s %s <number>"
                , usagePrefix, Const.SET_CONFIG, Const.RAIN_POINT));
        sender.sendMessage(String.format("%s雨が発生する高さ", descPrefix));
        sender.sendMessage(String.format("%s%s %s <block|potion|potionEffect|rain|mob>"
                , usagePrefix, Const.SET_CONFIG, Const.OFF_ACID_TARGET));
        sender.sendMessage(String.format("%s指定した設定をOFFに切り替える", descPrefix));
        sender.sendMessage(String.format("%s%s %s <block|potion|potionEffect|rain|mob>"
                , usagePrefix, Const.SET_CONFIG, Const.ON_ACID_TARGET));
        sender.sendMessage(String.format("%s指定した設定をONに切り替える", descPrefix));
        sender.sendMessage(String.format("%s%s"
                , usagePrefix, Const.SHOW_STATUS));
        sender.sendMessage(String.format("%s設定などゲームの状態を確認", descPrefix));

    }

    private int validateNum(CommandSender sender, String target) {
        // 不正な場合は-1を返す
        int num;
        try {
            num = Integer.parseInt(target);
        } catch (NumberFormatException e) {
            sender.sendMessage(DecolationConst.RED + "整数以外が入力されています");
            return -1;
        }
        if (num < 0) {
            sender.sendMessage(DecolationConst.RED + "0以上の整数を入力してください");
            return -1;
        }
        return num;
    }

    private boolean checkArgsNum(CommandSender sender, int argsLength, int validLength) {
        if (argsLength != validLength) {
            sender.sendMessage(DecolationConst.RED + "引数の数が不正です");
            return false;
        }
        return true;
    }

    private boolean setIntConfig(CommandSender sender, String[] args, int validLength, String configName){
        if (!checkArgsNum(sender, args.length, validLength)) return false;
        int ret = validateNum(sender, args[2]);
        if (ret == -1) return false;

        Config.intConf.put(configName, ret);
        sender.sendMessage(DecolationConst.GREEN + configName + "の値を" + Config.intConf.get(configName) + "に変更しました");
        Config.saveConfig(configName);
        return true;
    }
}