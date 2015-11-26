/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: luis.egidio@qub-it.com
 *
 * 
 * This file is part of FenixEdu Specifications.
 *
 * FenixEdu Specifications is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Specifications is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Specifications.  If not, see <http://www.gnu.org/licenses/>.
 */
		
package org.fenixedu.ulisboa.specifications.dto.evaluation;

import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.EvaluationConfiguration;
import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.academic.domain.OccupationPeriodReference;
import org.fenixedu.bennu.IBean;
import org.fenixedu.bennu.TupleDataSourceBean;
import org.fenixedu.ulisboa.specifications.domain.evaluation.EvaluationSeasonInformation;
import org.fenixedu.ulisboa.specifications.domain.evaluation.EvaluationSeasonRule;


public class EvaluationSeasonBean implements IBean {
	
	private EvaluationSeasonInformation evaluationSeasonInformation;
	private List<TupleDataSourceBean> evaluationSeasonInformationDataSource;
	private EvaluationConfiguration evaluationConfiguration;
	private List<TupleDataSourceBean> evaluationConfigurationDataSource;
	private EvaluationConfiguration evaluationConfigurationAsDefault;
	private List<TupleDataSourceBean> evaluationConfigurationAsDefaultDataSource;
	private EvaluationSeasonRule rules;
	private List<TupleDataSourceBean> rulesDataSource;
	private java.lang.String code;
	private org.fenixedu.commons.i18n.LocalizedString acronym;
	private org.fenixedu.commons.i18n.LocalizedString name;
	private boolean normal;
	private boolean improvement;
	private boolean special;
	private boolean specialAuthorization;
	private java.lang.Integer seasonOrder;
	private java.lang.Boolean active;
	private java.lang.Boolean requiresEnrolmentEvaluation;

	private List<OccupationPeriodReference> period;
	private List<TupleDataSourceBean> periodDataSource;
	
	public EvaluationSeasonInformation getEvaluationSeasonInformation(){
		return evaluationSeasonInformation;
	}
	public void setEvaluationSeasonInformation(EvaluationSeasonInformation value){
		evaluationSeasonInformation = value;
	}
	
	public List<TupleDataSourceBean> getEvaluationSeasonInformationDataSource(){
		return evaluationSeasonInformationDataSource;
	}
	public void setEvaluationSeasonInformationDataSource (List<EvaluationSeasonInformation> value){
		this.evaluationSeasonInformationDataSource = value.stream().map(x -> {
            TupleDataSourceBean tuple = new TupleDataSourceBean();
            tuple.setId(x.getExternalId()); //CHANGE_ME
            tuple.setText(x.toString()); //CHANGE_ME
            return tuple;
        }).collect(Collectors.toList());
	}
			
	public EvaluationConfiguration getEvaluationConfiguration(){
		return evaluationConfiguration;
	}
	public void setEvaluationConfiguration(EvaluationConfiguration value){
		evaluationConfiguration = value;
	}
	
	public List<TupleDataSourceBean> getEvaluationConfigurationDataSource(){
		return evaluationConfigurationDataSource;
	}
	public void setEvaluationConfigurationDataSource (List<EvaluationConfiguration> value){
		this.evaluationConfigurationDataSource = value.stream().map(x -> {
            TupleDataSourceBean tuple = new TupleDataSourceBean();
            tuple.setId(x.getExternalId()); //CHANGE_ME
            tuple.setText(x.toString()); //CHANGE_ME
            return tuple;
        }).collect(Collectors.toList());
	}
			
	public EvaluationConfiguration getEvaluationConfigurationAsDefault(){
		return evaluationConfigurationAsDefault;
	}
	public void setEvaluationConfigurationAsDefault(EvaluationConfiguration value){
		evaluationConfigurationAsDefault = value;
	}
	
	public List<TupleDataSourceBean> getEvaluationConfigurationAsDefaultDataSource(){
		return evaluationConfigurationAsDefaultDataSource;
	}
	public void setEvaluationConfigurationAsDefaultDataSource (List<EvaluationConfiguration> value){
		this.evaluationConfigurationAsDefaultDataSource = value.stream().map(x -> {
            TupleDataSourceBean tuple = new TupleDataSourceBean();
            tuple.setId(x.getExternalId()); //CHANGE_ME
            tuple.setText(x.toString()); //CHANGE_ME
            return tuple;
        }).collect(Collectors.toList());
	}
			
	public EvaluationSeasonRule getRules(){
		return rules;
	}
	public void setRules(EvaluationSeasonRule value){
		rules = value;
	}
	
	public List<TupleDataSourceBean> getRulesDataSource(){
		return rulesDataSource;
	}
	public void setRulesDataSource (List<EvaluationSeasonRule> value){
		this.rulesDataSource = value.stream().map(x -> {
            TupleDataSourceBean tuple = new TupleDataSourceBean();
            tuple.setId(x.getExternalId()); //CHANGE_ME
            tuple.setText(x.toString()); //CHANGE_ME
            return tuple;
        }).collect(Collectors.toList());
	}
			
	public java.lang.String getCode(){
		return code;
	}
	public void setCode(java.lang.String value){
		code = value;
	}
	public org.fenixedu.commons.i18n.LocalizedString getAcronym(){
		return acronym;
	}
	public void setAcronym(org.fenixedu.commons.i18n.LocalizedString value){
		acronym = value;
	}
	public org.fenixedu.commons.i18n.LocalizedString getName(){
		return name;
	}
	public void setName(org.fenixedu.commons.i18n.LocalizedString value){
		name = value;
	}
	public boolean getNormal(){
		return normal;
	}
	public void setNormal(boolean value){
		normal = value;
	}
	public boolean getImprovement(){
		return improvement;
	}
	public void setImprovement(boolean value){
		improvement = value;
	}
	public boolean getSpecial(){
		return special;
	}
	public void setSpecial(boolean value){
		special = value;
	}
	public boolean getSpecialAuthorization(){
		return specialAuthorization;
	}
	public void setSpecialAuthorization(boolean value){
		specialAuthorization = value;
	}
	public java.lang.Integer getSeasonOrder(){
		return seasonOrder;
	}
	public void setSeasonOrder(java.lang.Integer value){
		seasonOrder = value;
	}
	public java.lang.Boolean getActive(){
		return active;
	}
	public void setActive(java.lang.Boolean value){
		active = value;
	}
	public java.lang.Boolean getRequiresEnrolmentEvaluation(){
		return requiresEnrolmentEvaluation;
	}
	public void setRequiresEnrolmentEvaluation(java.lang.Boolean value){
		requiresEnrolmentEvaluation = value;
	}

	
	public List<OccupationPeriodReference> getPeriod(){
		return period;
	}
	public void setPeriod(List<OccupationPeriodReference> value){
		period = value;
	}
		public List<TupleDataSourceBean> getPeriodDataSource(){
			return periodDataSource;
		}
	public void setPeriodDataSource (List<OccupationPeriodReference> value){
		this.periodDataSource = value.stream().map(x -> {
            TupleDataSourceBean tuple = new TupleDataSourceBean();
            tuple.setId(x.getExternalId()); //CHANGE_ME
            tuple.setText(x.toString()); //CHANGE_ME
            return tuple;
        }).collect(Collectors.toList());
	}
	
	
	
	
	
	
	
	
	
	
	
    
	public EvaluationSeasonBean(){
	
	}
	
	public EvaluationSeasonBean(EvaluationSeason evaluationSeason){
//		this.setEvaluationSeasonInformation(evaluationSeason.getEvaluationSeasonInformation());
//		this.setEvaluationConfiguration(evaluationSeason.getEvaluationConfiguration());
//		this.setEvaluationConfigurationAsDefault(evaluationSeason.getEvaluationConfigurationAsDefault());
//		this.setRules(evaluationSeason.getRules());
		this.setCode(evaluationSeason.getCode());
		this.setAcronym(evaluationSeason.getAcronym());
		this.setName(evaluationSeason.getName());
		this.setNormal(evaluationSeason.getNormal());
		this.setImprovement(evaluationSeason.getImprovement());
		this.setSpecial(evaluationSeason.getSpecial());
		this.setSpecialAuthorization(evaluationSeason.getSpecialAuthorization());
//		this.setSeasonOrder(evaluationSeason.getSeasonOrder());
//		this.setActive(evaluationSeason.getActive());
//		this.setRequiresEnrolmentEvaluation(evaluationSeason.getRequiresEnrolmentEvaluation());
//		this.setPeriod(evaluationSeason.getPeriod());
		this.setCode(evaluationSeason.getCode());
		this.setAcronym(evaluationSeason.getAcronym());
		this.setName(evaluationSeason.getName());
		this.setNormal(evaluationSeason.getNormal());
		this.setImprovement(evaluationSeason.getImprovement());
		this.setSpecial(evaluationSeason.getSpecial());
		this.setSpecialAuthorization(evaluationSeason.getSpecialAuthorization());
//		this.setSeasonOrder(evaluationSeason.getSeasonOrder());
//		this.setActive(evaluationSeason.getActive());
//		this.setRequiresEnrolmentEvaluation(evaluationSeason.getRequiresEnrolmentEvaluation());
	}
	    
}
