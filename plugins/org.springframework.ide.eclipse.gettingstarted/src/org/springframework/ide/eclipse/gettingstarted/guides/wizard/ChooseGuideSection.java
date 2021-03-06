/*******************************************************************************
 *  Copyright (c) 2013 GoPivotal, Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *      GoPivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.springframework.ide.eclipse.gettingstarted.guides.wizard;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.springframework.ide.eclipse.gettingstarted.content.GettingStartedContent;
import org.springframework.ide.eclipse.gettingstarted.content.GettingStartedGuide;
import org.springframework.ide.eclipse.gettingstarted.content.GithubRepoContent;
import org.springframework.ide.eclipse.gettingstarted.wizard.ChooseOneSection;
import org.springframework.ide.eclipse.gettingstarted.wizard.ChooseOneSectionCombo;
import org.springframework.ide.eclipse.gettingstarted.wizard.ChooseOneSectionTable;
import org.springframework.ide.eclipse.gettingstarted.wizard.SelectionModel;
import org.springsource.ide.eclipse.commons.livexp.core.LiveExpression;
import org.springsource.ide.eclipse.commons.livexp.core.ValidationResult;
import org.springsource.ide.eclipse.commons.livexp.ui.PageSection;
import org.springsource.ide.eclipse.commons.livexp.ui.WizardPageSection;
import org.springsource.ide.eclipse.commons.livexp.ui.WizardPageWithSections;

/**
 * Allow choosing a guide in pull-down style combo box or table viewer.
 * 
 * @author Kris De Volder
 */
public class ChooseGuideSection extends WizardPageSection {
	
	private ChooseOneSection wrappee;
	private boolean useCombo = false;
	private SelectionModel<GettingStartedGuide> model;

	public ChooseGuideSection(
			WizardPageWithSections owner,
			SelectionModel<GettingStartedGuide> model
	) {
		super(owner);
		this.model = model;
	}

	/**
	 * Enable simplified UI where the guide is chosen via a combo
	 * which takes much less space (but is not as nice when there is
	 * a lot of content to choose from.
	 * <p>
	 * This option is disabled by default.
	 */
	public ChooseGuideSection useCombo(boolean enable) {
		useCombo = enable;
		return this;
	}

	@Override
	public LiveExpression<ValidationResult> getValidator() {
		return wrappee().getValidator();
	}

	private PageSection wrappee() {
		if (wrappee==null) {
			if (useCombo) {
				wrappee = new ChooseOneSectionCombo<GettingStartedGuide>(
						owner, "Guide", model, GettingStartedContent.getInstance().getGuides());
			} else {
				wrappee = new ChooseOneSectionTable<GettingStartedGuide>(
						owner, null, model, GettingStartedContent.getInstance().getGuides());
			}
			wrappee.setLabelProvider(new LabelProvider() {
				@Override
				public String getText(Object element) {
					if (element instanceof GettingStartedGuide) {
						GithubRepoContent gsg = (GithubRepoContent) element;
						return gsg.getDisplayName();
					}
					return super.getText(element);
				}
			});

		}
		return wrappee;
	}



	@Override
	public void createContents(Composite page) {
		wrappee().createContents(page);
	}
		
}
