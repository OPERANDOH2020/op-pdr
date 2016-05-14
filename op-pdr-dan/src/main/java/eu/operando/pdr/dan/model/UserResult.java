/*******************************************************************************
 *  # Copyright (c) 2016 {UPRC}.
 *  # All rights reserved. This program and the accompanying materials
 *  # are made available under the terms of the The MIT License (MIT).
 *  # which accompanies this distribution, and is available at
 *  # http://opensource.org/licenses/MIT
 *
 *  # Contributors:
 *  #    {Constantinos Patsakis} {UPRC}
 *  #    {Stamatis Glykos} {UPRC}
 *  #    {Constantinos Alexandris} {UPRC}
 *  # Initially developed in the context of OPERANDO EU project www.operando.eu 
 *******************************************************************************/

package eu.operando.pdr.dan.model;

public class UserResult {
	private UserGeneralData general_data;
	private UserAssuranceData assurance_data;
	private UserSocialContextData social_context_data;
	private UserClinicalData clinical_data;		
	private UserCareNeedsData care_needs_data;
	private UserCareLogData care_log_data;
	
	public UserGeneralData getGeneral_data() {
		return general_data;
	}
	public void setGeneral_data(UserGeneralData general_data) {
		this.general_data = general_data;
	}
	public UserAssuranceData getAssurance_data() {
		return assurance_data;
	}
	public void setAssurance_data(UserAssuranceData assurance_data) {
		this.assurance_data = assurance_data;
	}
	public UserSocialContextData getSocial_context_data() {
		return social_context_data;
	}
	public void setSocial_context_data(UserSocialContextData social_context_data) {
		this.social_context_data = social_context_data;
	}
	public UserClinicalData getClinical_data() {
		return clinical_data;
	}
	public void setClinical_data(UserClinicalData clinical_data) {
		this.clinical_data = clinical_data;
	}
	public UserCareNeedsData getCare_needs_data() {
		return care_needs_data;
	}
	public void setCare_needs_data(UserCareNeedsData care_needs_data) {
		this.care_needs_data = care_needs_data;
	}
	public UserCareLogData getCare_log_data() {
		return care_log_data;
	}
	public void setCare_log_data(UserCareLogData care_log_data) {
		this.care_log_data = care_log_data;
	}		
	
		
}
