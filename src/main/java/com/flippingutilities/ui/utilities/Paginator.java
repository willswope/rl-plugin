package com.flippingutilities.ui.utilities;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.ColorScheme;

@Slf4j
public class Paginator extends JPanel
{
	@Getter
	@Setter
	private int pageNumber = 1;
	private int totalPages;
	private JLabel statusText;
	private JLabel arrowRight;
	private JLabel arrowLeft;
	Runnable onPageChange;
	private static int PAGE_SIZE = 20;

	public Paginator(Runnable onPageChange)
	{
		this.onPageChange = onPageChange;
		this.statusText = new JLabel("Page 1234 of 1234", SwingUtilities.CENTER);
		this.arrowLeft = new JLabel(UIUtilities.ARROW_LEFT);
		this.arrowRight = new JLabel(UIUtilities.ARROW_RIGHT);
		this.arrowRight.setForeground(Color.blue);
		setLayout(new FlowLayout());
		add(arrowLeft);
		add(statusText);
		add(arrowRight);
		setBackground(ColorScheme.DARKER_GRAY_COLOR.darker());
		setBorder(new EmptyBorder(3, 0, 0, 0));
		arrowLeft.addMouseListener(onMouse(false));
		arrowRight.addMouseListener(onMouse(true));
	}

	public void updateTotalPages(int numItems)
	{
		if (numItems <= PAGE_SIZE) {
			totalPages = 1;
		}
		else {
			totalPages = (int) Math.ceil((float)numItems/PAGE_SIZE);
		}

		statusText.setText(String.format("Page %d of %d", pageNumber, totalPages));
	}

	private MouseAdapter onMouse(boolean isIncrease)
	{
		return new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				if (isIncrease)
				{
					if (pageNumber < totalPages)
					{
						pageNumber++;
						try
						{
							onPageChange.run();
						}
						catch (Exception exc)
						{
							log.info("couldn't increase page number cause callback failed");
							pageNumber--;
						}
					}
				}
				else
				{
					if (pageNumber > 1)
					{
						pageNumber--;
						try
						{
							onPageChange.run();
						}
						catch (Exception exc)
						{
							log.info("couldn't decrease page number cause callback failed");
							pageNumber++;
						}

					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
				if (isIncrease)
				{
					arrowRight.setIcon(UIUtilities.ARROW_RIGHT_HOVER);
				}
				else
				{
					arrowLeft.setIcon(UIUtilities.ARROW_LEFT_HOVER);
				}
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
				if (isIncrease)
				{
					arrowRight.setIcon(UIUtilities.ARROW_RIGHT);
				}
				else
				{
					arrowLeft.setIcon(UIUtilities.ARROW_LEFT);
				}
			}
		};
	}

	public <T> List<T> getCurrentPageItems(List<T> items)
	{
		List<T> pageItems = new ArrayList<>();
		int startIndex = (pageNumber - 1) * PAGE_SIZE;
		int endIndex = Math.min(startIndex + PAGE_SIZE, items.size());
		for (int i = startIndex; i < endIndex; i++)
		{
			pageItems.add(items.get(i));
		}
		return pageItems;
	}
}
