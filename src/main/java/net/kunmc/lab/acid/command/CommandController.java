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
                        args[1].equals(Const.DAMAGE_TERM)))) {
            completions.add("<数字>");
        } else if (args.length == 3 && args[1].equals(Const.OFF_ACID_TARGET)) {
            List<String> tmpCompletion = new ArrayList<>();
            for (Map.Entry<String, Boolean> happening : Config.booleanConf.entrySet()) {
                if (!happening.getValue()) {
                    tmpCompletion.add(happening.getKey());
                }
            }
            completions.addAll(tmpCompletion.stream().filter(e -> e.startsWith(args[2])).collect(Collectors.toList()));
        } else if (args.length == 3 && args[1].equals(Const.ON_ACID_TARGET)) {
            List<String> tmpCompletion = new ArrayList<>();
            for (Map.Entry<String, Boolean> acidTarget : Config.booleanConf.entrySet()) {
                if (acidTarget.getValue()) {
                    tmpCompletion.add(acidTarget.getKey());
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
                        if (!checkArgsNum(sender, args.length, 3)) return true;
                        int ret = validateNum(sender, args[2]);
                        if (ret == -1) return true;

                        Config.intConf.put(Const.DAMAGE, ret);
                        sender.sendMessage(DecolationConst.GREEN + Const.DAMAGE + "の値を" + Config.intConf.get(Const.DAMAGE) + "に変更しました");
                        break;
                    case Const.SPLASH_DAMAGE:
                        if (!checkArgsNum(sender, args.length, 3)) return true;
                        ret = validateNum(sender, args[2]);
                        if (ret == -1) return true;

                        Config.intConf.put(Const.SPLASH_DAMAGE, ret);
                        sender.sendMessage(DecolationConst.GREEN + Const.SPLASH_DAMAGE + "の値を" + Config.intConf.get(Const.SPLASH_DAMAGE) + "に変更しました");
                        break;
                    case Const.DAMAGE_TERM:
                        if (!checkArgsNum(sender, args.length, 3)) return true;
                        ret = validateNum(sender, args[2]);
                        if (ret == -1) return true;

                        Config.intConf.put(Const.DAMAGE_TERM, ret);
                        sender.sendMessage(DecolationConst.GREEN + Const.DAMAGE_TERM + "の値を" + Config.intConf.get(Const.DAMAGE_TERM) + "に変更しました");
                        break;
                    case Const.ON_ACID_TARGET:
                        if (!checkArgsNum(sender, args.length, 3)) return true;
                        if (!Config.booleanConf.containsKey(args[2]))
                            sender.sendMessage(DecolationConst.RED + "存在しないHappeningです");
                        if (Config.booleanConf.get(args[2])) sender.sendMessage(DecolationConst.AQUA + "すでにONになっています");
                        Config.booleanConf.put(args[2], true);
                        break;
                    case Const.OFF_ACID_TARGET:
                        if (!checkArgsNum(sender, args.length, 3)) return true;
                        if (!Config.booleanConf.containsKey(args[2]))
                            sender.sendMessage(DecolationConst.RED + "存在しないHappeningです");
                        if (!Config.booleanConf.get(args[2])) sender.sendMessage(DecolationConst.AQUA + "すでにOFFになっています");
                        Config.booleanConf.put(args[2], false);
                        break;
                }
                break;
            case Const.SHOW_STATUS:
                if (args.length != 1) {
                    sender.sendMessage(DecolationConst.RED + "引数が不要なコマンドです");
                    return true;
                }
                sender.sendMessage(DecolationConst.GREEN + "設定値一覧");
                List<String> acidTargets = new ArrayList<>();
                for (Map.Entry<String, Boolean> acidTarget : Config.booleanConf.entrySet()) {
                    if (acidTarget.getValue()) acidTargets.add(acidTarget.getKey());
                }
                String prefix = "  ";
                for (Map.Entry<String, Integer> param: Config.intConf.entrySet()) {
                    sender.sendMessage(String.format("%s%s: %s", prefix, param.getKey(), param.getValue()));
                }
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
        sender.sendMessage(String.format("%ポーションなどを浴びた時に受けるダメージ", descPrefix));
        sender.sendMessage(String.format("%s%s %s <number>"
                , usagePrefix, Const.SET_CONFIG, Const.DAMAGE_TERM));
        sender.sendMessage(String.format("%s水や雨で濡れている時にダメージを受ける間隔(Tick)", descPrefix));
        sender.sendMessage(String.format("%s%s %s <block|potion|rain>"
                , usagePrefix, Const.SET_CONFIG, Const.OFF_ACID_TARGET));
        sender.sendMessage(String.format("%sダメージを受ける対象をOFF", descPrefix));
        sender.sendMessage(String.format("%s%s %s <block|potion|rain>"
                , usagePrefix, Const.SET_CONFIG, Const.ON_ACID_TARGET));
        sender.sendMessage(String.format("%sダメージを受ける対象をON", descPrefix));
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


}