package net.minefury.domains.menu.domaincreate.sub;

import net.minefury.domains.MineDomains;
import net.minefury.domains.exceptions.ConfigMenuDefinitionException;
import net.minefury.domains.menu.domaincreate.DomainCreateMenu;
import net.minefury.domains.menu.objects.AnvilMenu;
import net.minefury.domains.menu.objects.MenuIcon;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DomainCreateEnterName extends AnvilMenu {

    private DomainCreateMenu mainCreateMenu;

    public DomainCreateEnterName(Player player, DomainCreateMenu mainCreateMenu) {
        super(player, "anvil-domain-enter-name");
        this.mainCreateMenu = mainCreateMenu;
    }

    @Override
    public void open() throws ConfigMenuDefinitionException {
        new AnvilGUI.Builder().onClose(p -> {
                    handleMenuClose();
                })
                .onComplete((p, text) -> {

                    if (isNameTooShort(text)) {
                        return AnvilGUI.Response.text("Name is too short.");
                    }

                    if (isNameTooLong(text)) {
                        return AnvilGUI.Response.text("Name is too long.");
                    }

                    if (hasIllegalCharacters(text)) {
                        return AnvilGUI.Response.text("Invalid domain name.");
                    }

                    mainCreateMenu.setDomainName(text);
                    mainCreateMenu.open();
                    return AnvilGUI.Response.close();
                })
                .itemLeft(MenuIcon.fromConfig(menuPath + ".item").getItem())
                .title(getMenuName())
                .plugin(MineDomains.getInstance())
                .open(player);
    }

    @Override
    public void handleMenuClose() {
    }

    private boolean isNameTooShort(String s) {
        return s.length() < 3;
    }

    private boolean isNameTooLong(String s) {
        return s.length() > 16;
    }

    private boolean hasIllegalCharacters(String s) {
        Pattern pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(s);

        return matcher.find();
    }
}
