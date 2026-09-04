package com.mod.mozaik.client.widgets;

import com.mod.mozaik.client.ModKeyMappings;
import com.mod.mozaik.util.IMozaikKeyMapping;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BackPortedEditBox extends AbstractWidget implements Renderable {
	private static final WidgetSprites SPRITES = new WidgetSprites(ResourceLocation.withDefaultNamespace("widget/text_field"), ResourceLocation.withDefaultNamespace("widget/text_field_highlighted"));
	public static final int BACKWARDS = -1;
	public static final int FORWARDS = 1;
	public static final int DEFAULT_TEXT_COLOR = -2039584;
	public static final Style DEFAULT_HINT_STYLE;
	public static final Style SEARCH_HINT_STYLE;
	private final Font font;
	private String value;
	private int maxLength;
	private boolean bordered;
	private boolean canLoseFocus;
	private boolean isEditable;
	private boolean centered;
	private boolean textShadow;
	private boolean invertHighlightedTextColor;
	private int displayPos;
	private int cursorPos;
	private int highlightPos;
	private int textColor;
	private int textColorUneditable;
	private @Nullable String suggestion;
	private @Nullable Consumer<String> responder;
	private final List<TextFormatter> formatters;
	private @Nullable Component hint;
	private long focusedTime;
	private int textX;
	private int textY;

	public BackPortedEditBox(Font font, Component narration) {
		this(font, 150, 20, narration);
	}

	public BackPortedEditBox(Font font, int width, int height, Component narration) {
		this(font, 0, 0, width, height, narration);
	}

	public BackPortedEditBox(Font font, int x, int y, int width, int height, Component narration) {
		this(font, x, y, width, height, null, narration);
	}

	public BackPortedEditBox(Font font, int x, int y, int width, int height, @Nullable BackPortedEditBox oldBox, Component narration) {
		super(x, y, width, height, narration);
		this.value = "";
		this.maxLength = 32;
		this.bordered = true;
		this.canLoseFocus = true;
		this.isEditable = true;
		this.centered = false;
		this.textShadow = true;
		this.invertHighlightedTextColor = true;
		this.textColor = -2039584;
		this.textColorUneditable = -9408400;
		this.formatters = new ArrayList();
		this.focusedTime = Util.getMillis();
		this.font = font;
		if (oldBox != null) {
			this.setValue(oldBox.getValue());
		}

		this.updateTextPosition();
	}

	public void setResponder(Consumer<String> responder) {
		this.responder = responder;
	}

	public void addFormatter(TextFormatter formatter) {
		this.formatters.add(formatter);
	}

	protected MutableComponent createNarrationMessage() {
		Component message = this.getMessage();
		return Component.translatable("gui.narrate.editBox", message, this.value);
	}

	public void setValue(String value) {
		if (value.length() > this.maxLength) {
			this.value = value.substring(0, this.maxLength);
		} else {
			this.value = value;
		}

		this.moveCursorToEnd(false);
		this.setHighlightPos(this.cursorPos);
		this.onValueChange(value);
	}

	public String getValue() {
		return this.value;
	}

	public String getHighlighted() {
		int start = Math.min(this.cursorPos, this.highlightPos);
		int end = Math.max(this.cursorPos, this.highlightPos);
		return this.value.substring(start, end);
	}

	public void setX(int x) {
		super.setX(x);
		this.updateTextPosition();
	}

	public void setY(int y) {
		super.setY(y);
		this.updateTextPosition();
	}

	public void insertText(String input) {
		int start = Math.min(this.cursorPos, this.highlightPos);
		int end = Math.max(this.cursorPos, this.highlightPos);
		int maxInsertionLength = this.maxLength - this.value.length() - (start - end);
		if (maxInsertionLength > 0) {
			String text = StringUtil.filterText(input);
			int insertionLength = text.length();
			if (maxInsertionLength < insertionLength) {
				if (Character.isHighSurrogate(text.charAt(maxInsertionLength - 1))) {
					--maxInsertionLength;
				}

				text = text.substring(0, maxInsertionLength);
				insertionLength = maxInsertionLength;
			}

			this.value = (new StringBuilder(this.value)).replace(start, end, text).toString();
			this.setCursorPosition(start + insertionLength);
			this.setHighlightPos(this.cursorPos);
			this.onValueChange(this.value);
		}

	}

	private void onValueChange(String value) {
		if (this.responder != null) {
			this.responder.accept(value);
		}

		this.updateTextPosition();
	}

	private void deleteText(int dir, boolean wholeWord) {
		if (wholeWord) {
			this.deleteWords(dir);
		} else {
			this.deleteChars(dir);
		}

	}

	public void deleteWords(int dir) {
		if (!this.value.isEmpty()) {
			if (this.highlightPos != this.cursorPos) {
				this.insertText("");
			} else {
				this.deleteCharsToPos(this.getWordPosition(dir));
			}
		}

	}

	public void deleteChars(int dir) {
		this.deleteCharsToPos(this.getCursorPos(dir));
	}

	public void deleteCharsToPos(int pos) {
		if (!this.value.isEmpty()) {
			if (this.highlightPos != this.cursorPos) {
				this.insertText("");
			} else {
				int start = Math.min(pos, this.cursorPos);
				int end = Math.max(pos, this.cursorPos);
				if (start != end) {
					this.value = (new StringBuilder(this.value)).delete(start, end).toString();
					this.setCursorPosition(start);
					this.onValueChange(this.value);
					this.moveCursorTo(start, false);
				}
			}
		}

	}

	public int getWordPosition(int dir) {
		return this.getWordPosition(dir, this.getCursorPosition());
	}

	private int getWordPosition(int dir, int from) {
		return this.getWordPosition(dir, from, true);
	}

	private int getWordPosition(int dir, int from, boolean stripSpaces) {
		int result = from;
		boolean reverse = dir < 0;
		int abs = Math.abs(dir);

		for (int i = 0; i < abs; ++i) {
			if (!reverse) {
				int length = this.value.length();
				result = this.value.indexOf(32, result);
				if (result == -1) {
					result = length;
				} else {
					while (stripSpaces && result < length && this.value.charAt(result) == ' ') {
						++result;
					}
				}
			} else {
				while (stripSpaces && result > 0 && this.value.charAt(result - 1) == ' ') {
					--result;
				}

				while (result > 0 && this.value.charAt(result - 1) != ' ') {
					--result;
				}
			}
		}

		return result;
	}

	public void moveCursor(int dir, boolean hasShiftDown) {
		this.moveCursorTo(this.getCursorPos(dir), hasShiftDown);
	}

	private int getCursorPos(int dir) {
		return Util.offsetByCodepoints(this.value, this.cursorPos, dir);
	}

	public void moveCursorTo(int dir, boolean extendSelection) {
		this.setCursorPosition(dir);
		if (!extendSelection) {
			this.setHighlightPos(this.cursorPos);
		}

		this.updateTextPosition();
	}

	public void setCursorPosition(int pos) {
		this.cursorPos = Mth.clamp(pos, 0, this.value.length());
		this.scrollTo(this.cursorPos);
	}

	public void moveCursorToStart(boolean hasShiftDown) {
		this.moveCursorTo(0, hasShiftDown);
	}

	public void moveCursorToEnd(boolean hasShiftDown) {
		this.moveCursorTo(this.value.length(), hasShiftDown);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (this.isActive() && this.isFocused()) {
			switch (keyCode) {
				case 259:
					if (this.isEditable) {
						this.deleteText(-1, IMozaikKeyMapping.hasControlDown(modifiers));
					}

					return true;
				case 260:
				case 264:
				case 265:
				case 266:
				case 267:
				default:
					if (IMozaikKeyMapping.matches(ModKeyMappings.SELECT_ALL, keyCode, scanCode, modifiers)) {
						this.moveCursorToEnd(false);
						this.setHighlightPos(0);
						return true;
					} else if (IMozaikKeyMapping.hasControlDown(modifiers) && keyCode == GLFW.GLFW_KEY_C) {
						Minecraft.getInstance().keyboardHandler.setClipboard(this.getHighlighted());
						return true;
					} else if (IMozaikKeyMapping.hasControlDown(modifiers) && keyCode == GLFW.GLFW_KEY_V) {
						if (this.isEditable()) {
							this.insertText(Minecraft.getInstance().keyboardHandler.getClipboard());
						}

						return true;
					} else {
						if (IMozaikKeyMapping.hasControlDown(modifiers) && keyCode == GLFW.GLFW_KEY_X) {
							Minecraft.getInstance().keyboardHandler.setClipboard(this.getHighlighted());
							if (this.isEditable()) {
								this.insertText("");
							}

							return true;
						}

						return false;
					}
				case 261:
					if (this.isEditable) {
						this.deleteText(1, IMozaikKeyMapping.hasControlDown(modifiers));
					}

					return true;
				case 262:
					if (IMozaikKeyMapping.hasControlDown(modifiers)) {
						this.moveCursorTo(this.getWordPosition(1), IMozaikKeyMapping.hasShiftDown(modifiers));
					} else {
						this.moveCursor(1, IMozaikKeyMapping.hasShiftDown(modifiers));
					}

					return true;
				case 263:
					if (IMozaikKeyMapping.hasControlDown(modifiers)) {
						this.moveCursorTo(this.getWordPosition(-1), IMozaikKeyMapping.hasShiftDown(modifiers));
					} else {
						this.moveCursor(-1, IMozaikKeyMapping.hasShiftDown(modifiers));
					}

					return true;
				case 268:
					this.moveCursorToStart(IMozaikKeyMapping.hasShiftDown(modifiers));
					return true;
				case 269:
					this.moveCursorToEnd(IMozaikKeyMapping.hasShiftDown(modifiers));
					return true;
			}
		} else {
			return false;
		}
	}

	public boolean canConsumeInput() {
		return this.isActive() && this.isFocused() && this.isEditable();
	}

	@Override
	public boolean charTyped(char codePoint, int modifiers) {
		if (!this.canConsumeInput()) {
			return false;
		} else if (StringUtil.isAllowedChatCharacter(codePoint)) {
			if (this.isEditable) {
				this.insertText(Character.toString(codePoint));
			}
			return true;
		} else {
			return false;
		}
	}

	private int findClickedPositionInText(double x) {
		int positionInText = Math.min(Mth.floor(x) - this.textX, this.getInnerWidth());
		String displayed = this.value.substring(this.displayPos);
		return this.displayPos + this.font.plainSubstrByWidth(displayed, positionInText).length();
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		this.moveCursorTo(this.findClickedPositionInText(mouseX), Screen.hasShiftDown());
	}

	@Override
	protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
		this.moveCursorTo(this.findClickedPositionInText(mouseX), true);
	}

	@Override
	public void playDownSound(SoundManager soundManager) {
	}

	@Override
	protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		if (this.isVisible()) {
			if (this.isBordered()) {
				ResourceLocation sprite = SPRITES.get(this.isActive(), this.isFocused());
				graphics.blitSprite(sprite, this.getX(), this.getY(), this.getWidth(), this.getHeight());
			}

			int color = this.isEditable ? this.textColor : this.textColorUneditable;
			int relCursorPos = this.cursorPos - this.displayPos;
			String displayed = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), this.getInnerWidth());
			boolean cursorOnScreen = relCursorPos >= 0 && relCursorPos <= displayed.length();
			boolean showCursor = this.isFocused() && (Util.getMillis() - this.focusedTime) / 300L % 2L == 0L && cursorOnScreen;
			int drawX = this.textX;
			int relHighlightPos = Mth.clamp(this.highlightPos - this.displayPos, 0, displayed.length());
			if (!displayed.isEmpty()) {
				String half = cursorOnScreen ? displayed.substring(0, relCursorPos) : displayed;
				FormattedCharSequence charSequence = this.applyFormat(half, this.displayPos);
				graphics.drawString(this.font, charSequence, drawX, this.textY, color, this.textShadow);
				drawX += this.font.width(charSequence) + 1;
			}

			boolean insert = this.cursorPos < this.value.length() || this.value.length() >= this.getMaxLength();
			int cursorX = drawX;
			if (!cursorOnScreen) {
				cursorX = relCursorPos > 0 ? this.textX + this.width : this.textX;
			} else if (insert) {
				cursorX = drawX - 1;
				--drawX;
			}

			if (!displayed.isEmpty() && cursorOnScreen && relCursorPos < displayed.length()) {
				graphics.drawString(this.font, this.applyFormat(displayed.substring(relCursorPos), this.cursorPos), drawX, this.textY, color, this.textShadow);
			}

			if (this.hint != null && displayed.isEmpty() && !this.isFocused()) {
				graphics.drawString(this.font, this.hint, drawX, this.textY, color);
			}

			if (!insert && this.suggestion != null) {
				graphics.drawString(this.font, this.suggestion, cursorX - 1, this.textY, -8355712, this.textShadow);
			}

			if (relHighlightPos != relCursorPos) {
				int highlightX = this.textX + this.font.width(displayed.substring(0, relHighlightPos));
				this.renderHighlight(graphics, Math.min(cursorX, this.getX() + this.width), this.textY - 1, Math.min(highlightX - 1, this.getX() + this.width), this.textY + 1 + 9);
			}

			if (showCursor) {
				if (insert) {
					graphics.fill(RenderType.guiOverlay(), cursorX, this.textY - 1, cursorX + 1, this.textY + 1 + 9, -3092272);
				} else {
					graphics.drawString(this.font, "_", cursorX, this.textY, color);
				}
			}
		}

	}

	private void renderHighlight(GuiGraphics guiGraphics, int minX, int minY, int maxX, int maxY) {
		if (minX < maxX) {
			int i = minX;
			minX = maxX;
			maxX = i;
		}

		if (minY < maxY) {
			int j = minY;
			minY = maxY;
			maxY = j;
		}

		if (maxX > this.getX() + this.width) {
			maxX = this.getX() + this.width;
		}

		if (minX > this.getX() + this.width) {
			minX = this.getX() + this.width;
		}

		guiGraphics.fill(RenderType.guiTextHighlight(), minX, minY, maxX, maxY, -16776961);
	}

	private FormattedCharSequence applyFormat(String text, int offset) {
		for (TextFormatter formatter : this.formatters) {
			FormattedCharSequence formattedCharSequence = formatter.format(text, offset);
			if (formattedCharSequence != null) {
				return formattedCharSequence;
			}
		}

		return FormattedCharSequence.forward(text, Style.EMPTY);
	}

	private void updateTextPosition() {
		if (this.font != null) {
			String displayed = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), this.getInnerWidth());
			this.textX = this.getX() + (this.isCentered() ? (this.getWidth() - this.font.width(displayed)) / 2 : (this.bordered ? 4 : 0));
			this.textY = this.bordered ? this.getY() + (this.height - 8) / 2 : this.getY();
		}

	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
		if (this.value.length() > maxLength) {
			this.value = this.value.substring(0, maxLength);
			this.onValueChange(this.value);
		}

	}

	private int getMaxLength() {
		return this.maxLength;
	}

	public int getCursorPosition() {
		return this.cursorPos;
	}

	public boolean isBordered() {
		return this.bordered;
	}

	public void setBordered(boolean bordered) {
		this.bordered = bordered;
		this.updateTextPosition();
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public void setTextColorUneditable(int textColorUneditable) {
		this.textColorUneditable = textColorUneditable;
	}

	public void setFocused(boolean focused) {
		if (this.canLoseFocus || focused) {
			super.setFocused(focused);
			if (focused) {
				this.focusedTime = Util.getMillis();
			}

			/*if (this.isEditable()) {
				Minecraft.getInstance().onTextInputFocusChange(this, focused);
			}*/
		}

	}

	private boolean isEditable() {
		return this.isEditable;
	}

	private boolean isCentered() {
		return this.centered;
	}

	public void setCentered(boolean centered) {
		this.centered = centered;
		this.updateTextPosition();
	}

	public void setTextShadow(boolean textShadow) {
		this.textShadow = textShadow;
	}

	public void setInvertHighlightedTextColor(boolean invertHighlightedTextColor) {
		this.invertHighlightedTextColor = invertHighlightedTextColor;
	}

	public int getInnerWidth() {
		return this.isBordered() ? this.width - 8 : this.width;
	}

	public void setHighlightPos(int pos) {
		this.highlightPos = Mth.clamp(pos, 0, this.value.length());
		this.scrollTo(this.highlightPos);
	}

	private void scrollTo(int pos) {
		if (this.font != null) {
			this.displayPos = Math.min(this.displayPos, this.value.length());
			int innerWidth = this.getInnerWidth();
			String displayed = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), innerWidth);
			int lastPos = displayed.length() + this.displayPos;
			if (pos == this.displayPos) {
				this.displayPos -= this.font.plainSubstrByWidth(this.value, innerWidth, true).length();
			}

			if (pos > lastPos) {
				this.displayPos += pos - lastPos;
			} else if (pos <= this.displayPos) {
				this.displayPos -= this.displayPos - pos;
			}

			this.displayPos = Mth.clamp(this.displayPos, 0, this.value.length());
		}

	}

	public void setCanLoseFocus(boolean canLoseFocus) {
		this.canLoseFocus = canLoseFocus;
	}

	public boolean isVisible() {
		return this.visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setSuggestion(@Nullable String suggestion) {
		this.suggestion = suggestion;
	}

	public int getScreenX(int charIndex) {
		return charIndex > this.value.length() ? this.getX() : this.getX() + this.font.width(this.value.substring(0, charIndex));
	}

	public void updateWidgetNarration(NarrationElementOutput output) {
		output.add(NarratedElementType.TITLE, this.createNarrationMessage());
	}

	public void setHint(Component hint) {
		boolean hasNoStyle = hint.getStyle().equals(Style.EMPTY);
		this.hint = hasNoStyle ? hint.copy().withStyle(DEFAULT_HINT_STYLE) : hint;
	}

	static {
		DEFAULT_HINT_STYLE = Style.EMPTY.withColor(ChatFormatting.DARK_GRAY);
		SEARCH_HINT_STYLE = Style.EMPTY.applyFormats(ChatFormatting.GRAY, ChatFormatting.ITALIC);
	}

	@FunctionalInterface
	public interface TextFormatter {
		@Nullable FormattedCharSequence format(String var1, int var2);
	}
}
