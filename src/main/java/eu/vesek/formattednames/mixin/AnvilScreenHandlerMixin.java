package eu.vesek.formattednames.mixin;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import eu.pb4.placeholders.api.TextParserUtils;

@SuppressWarnings("deprecation")
@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {
	public AnvilScreenHandlerMixin(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
		super(type, syncId, playerInventory, context);
	}

	@Shadow
    private String newItemName;
	
	@ModifyArg(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;set(Lnet/minecraft/component/ComponentType;Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 0), index = 1)
    private Object updateResult(Object obj) {
        return customTextFormatting();
    }

	@ModifyArg(method = "setNewItemName", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;set(Lnet/minecraft/component/ComponentType;Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 0), index = 1)
    private Object setNewItemName(Object obj) {
        return customTextFormatting();
    }

    @Unique
    private Text customTextFormatting() {
        Text formattedText = TextParserUtils.formatTextSafe(this.newItemName);
        // check if the new item name has a custom format
        boolean hasFormat = switch (formattedText.getSiblings().size()) {
            case 0 -> false;
            // this detection fails if the item name is "<reset>Itemname"
            case 1 -> !formattedText.getSiblings().get(0).getStyle().equals(Style.EMPTY);
            default -> true;
        };
        // if the new item name has a custom format, disable italics
        if (hasFormat) return formattedText.copy().setStyle(formattedText.getStyle().withItalic(false));
        return formattedText;
    }
}