import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { AffectationsService } from '../../affectations';
import { HttpResponse, HttpClient } from '@angular/common/http';
import { IVue2 } from 'app/shared/model/vue-2.model';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { Vue3ToolsService } from './vue-3.Tools.service';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

type EntityResponseType = HttpResponse<IVue2>;

@Injectable({ providedIn: 'root' })
export class Vue3Service {
    public resourceUrl = SERVER_API_URL + 'api/affectations';
    public tabItemsCollaborator = [];
    public language: string;

    // terminer le travail de traduction de la timeline (nom projet, name Project, ...)
    constructor(private affectationsService: AffectationsService,
        protected http: HttpClient,
        private vue3ToolsService: Vue3ToolsService,
        private languageService: JhiLanguageService,
        private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.language = languageKey;
                this.languageService.changeLanguage(languageKey);
            }
        });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IVue2>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    public getCollaboratorsToServer() {
        return new Promise((resolve, reject) => {
            this.affectationsService.query().subscribe(
                (res: any) => {
                    let storeobj: any[];
                    storeobj = res.body;
                    resolve(this.createTimeLineGroupsFromAffectations(storeobj));
                },
                (error: any) => reject(error)
            );
        });
    }

    public createTimeLineGroupsFromAffectations(storeobj: any) {
        let CollaboratorsAPI = [];
        const image: String = '&#x25B6';
        let titleCompetences: String;
        if (String(this.language).valueOf() === String('fr').valueOf()) {
             titleCompetences = ', Compétences:';
        } else {
            titleCompetences = ', competencies:';
        }
        for (let i = 0; i < storeobj.length; i++) {
            CollaboratorsAPI = CollaboratorsAPI.concat([
                {
                    id: storeobj[i].collaborator.id,
                    NameCollaborator: storeobj[i].collaborator.nomCollaborator + ' ' + storeobj[i].collaborator.prenomCollaborator,
                    imageLabel: image,
                    content: image + ' ' + storeobj[i].collaborator.nomCollaborator + ' ' + storeobj[i].collaborator.prenomCollaborator,
                    visible: true,
                    title:
                        'Trigramme: ' +
                        storeobj[i].collaborator.trigramme +
                        ', Email: ' +
                        storeobj[i].collaborator.email +
                        titleCompetences +
                        storeobj[i].collaborator.competencies,
                    subgroupOrder: (a: any, b: any) => {
                        return b.subgroup - a.subgroup;
                    }
                }
            ]);
        }
        for (let i = 0; i < CollaboratorsAPI.length; i++) {
            for (let j = i + 1; j < CollaboratorsAPI.length; j++) {
                if (CollaboratorsAPI[i].id === CollaboratorsAPI[j].id) {
                    CollaboratorsAPI.splice(j, 1);
                    j = j - 1;
                }
            }
        }
        CollaboratorsAPI.sort(this.tri);
        CollaboratorsAPI = this.addTotalsToGroups(CollaboratorsAPI);
        for (let i = 0; i < CollaboratorsAPI.length; i++) {
            CollaboratorsAPI[i].id = i + 1;
        }
        return CollaboratorsAPI;
    }

    public addTotalsToGroups(CollaboratorsAPI: any[]) {
        for (let i = 0; i < CollaboratorsAPI.length; i++) {
            CollaboratorsAPI.splice(i + 1, 0, {
                id: CollaboratorsAPI[i].id + 1,
                NameCollaborator: 'Total',
                content: 'Total',
                className: 'gris-ligne',
                visible: false,
                subgroupOrder: (a, b) => {
                    return b.subgroup - a.subgroup;
                }
            });
            i = i + 1;
        }
        return CollaboratorsAPI;
    }

    public tri(a: any, b: any): number {
        if (a.content < b.content) {
            return -1;
        } else if (a.content === b.content) {
            return 0;
        } else {
            return 1;
        }
    }

    public getProjectsToServerWithoutDetails() {
        return new Promise((resolve, reject) => {
            this.getCollaboratorsToServer().then(
                (resData: any[]) => {
                    const collaboratorsData = resData;
                    this.affectationsService.query().subscribe((res: any) => {
                        const tableResult = this.createTimeLineProjects(collaboratorsData, res.body);
                        const ProjectsAPI = tableResult[0];
                        resolve(ProjectsAPI);
                    });
                },
                (error: any) => reject(error)
            );
        });
    }

    public getIdCollaboratorFromNameCollaborator(NamelabelCollaborator: String, listCollaborator: any[]): number {
        for (let j = 0; j < listCollaborator.length; j++) {
            if (String(NamelabelCollaborator).valueOf() === String(listCollaborator[j].content).valueOf()) {
                return listCollaborator[j].id;
            }
        }
    }

    public selectItembyInterval(ProjectsAPI: any[], debutInterval: Date, finInterval: Date, groupId: number): any[] {
        let visid = [];
        for (let i = 0; i < ProjectsAPI.length; i++) {
            if (ProjectsAPI[i].group === groupId) {
                if (new Date(ProjectsAPI[i].start) < finInterval && new Date(ProjectsAPI[i].end) > debutInterval) {
                    visid = visid.concat([ProjectsAPI[i].id]);
                }
            }
        }
        return visid;
    }

    public addChargeItemsToTimeline(CollaboratorCharac: any[], ProjectsAPI: any[], lastId: number) {
        let l = 0;
        let visid: any[];
        for (let i = 0; i < CollaboratorCharac.length; i++) {
            for (let j = 0; j < CollaboratorCharac[i].tableTime.length - 1; j++) {
                visid = [];
                visid = this.selectItembyInterval(
                    ProjectsAPI,
                    CollaboratorCharac[i].tableTime[j],
                    CollaboratorCharac[i].tableTime[j + 1],
                    2 * i + 1
                );
                ProjectsAPI = ProjectsAPI.concat([
                    {
                        id: lastId + 1 + l,
                        content: this.sommeChargeBySlectedInterval(visid, ProjectsAPI) + '%',
                        className: 'charge',
                        start: CollaboratorCharac[i].tableTime[j],
                        end: CollaboratorCharac[i].tableTime[j + 1],
                        group: 2 * i + 2,
                        subgroup: 2 * i + 2,
                        title:
                            '<b>Charge</b> (' +
                            moment(CollaboratorCharac[i].tableTime[j]).format('MM/DD/YYYY') +
                            ' - ' +
                            moment(CollaboratorCharac[i].tableTime[j + 1]).format('MM/DD/YYYY') +
                            '): ' +
                            this.sommeChargeByInterval(visid) +
                            '%',
                        editable: false
                    }
                ]);
                l = l + 1;
            }
        }
        return ProjectsAPI;
    }

    public createTimeLineProjects(collaboratorsData: any[], resbody: any): any[] {
        let storeobj: any[];
        storeobj = resbody;
        let ProjectsAPI = [];
        let k = 0;
        let CollaboratorCharac = [];
        this.tabItemsCollaborator = [];
        let titleProject: String;
        if (String(this.language).valueOf() === String('fr').valueOf()) {
            titleProject = '</br> <b>Nom Projet</b>: ';
        } else {
            titleProject = '</br> <b>Name Project</b>: ';
        }
        for (let i = 0; i < collaboratorsData.length; i++) {
            let tabdate = [];
            if (String(collaboratorsData[i].NameCollaborator).valueOf() !== String('Total').valueOf()) {
                for (let j = 0; j < storeobj.length; j++) {
                    if (
                        String(collaboratorsData[i].NameCollaborator).valueOf() ===
                        String(storeobj[j].collaborator.nomCollaborator + ' ' + storeobj[j].collaborator.prenomCollaborator).valueOf()
                    ) {
                        k = k + 1;
                        this.tabItemsCollaborator = this.tabItemsCollaborator.concat([
                            {
                                idItem: k,
                                idProject: storeobj[j].project.id,
                                idAffectation: storeobj[j].id,
                                idCollaborator: storeobj[j].collaborator.id,
                                charge: storeobj[j].charge,
                                NameProject: storeobj[j].project.nameProject,
                                idGroup: i + 1
                            }
                        ]);
                        ProjectsAPI = ProjectsAPI.concat([
                            {
                                id: k,
                                NameProject: storeobj[j].project.nameProject,
                                NameCollaborator: collaboratorsData[i].NameCollaborator,
                                charge: storeobj[j].charge,
                                content:
                                    '<div class="content"><span class="content-titre"><img src="../../../content/images/folder-open.svg" style="width:20px; height:20px;"> ' +
                                    storeobj[j].project.nameProject +
                                    '</span> <span class="content-percent"> ' +
                                    storeobj[j].charge +
                                    '%</span></div>',
                                className: storeobj[j].color,
                                start: storeobj[j].dateDebut,
                                end: storeobj[j].dateFin,
                                group: i + 1,
                                subgroup: k,
                                title:
                                    '<div id="' +
                                    storeobj[j].color +
                                    '" ' +
                                    'style="text-align:center"><img src="../../../content/images/account.svg" style="width:20px; height:20px;"> <b>' +
                                    collaboratorsData[i].NameCollaborator +
                                    '</b> </div> <b>Date</b>: ' +
                                    storeobj[j].dateDebut.format('MM/DD/YYYY') +
                                    ' - ' +
                                    storeobj[j].dateFin.format('MM/DD/YYYY') +
                                    titleProject + storeobj[j].project.nameProject +
                                    '</br> <b>Charge</b>: ' +
                                    storeobj[j].charge +
                                    '%'
                            }
                        ]);
                        tabdate = tabdate.concat([storeobj[j].dateDebut.format('MM/DD/YYYY')]);
                        tabdate = tabdate.concat([storeobj[j].dateFin.format('MM/DD/YYYY')]);
                    }
                }
                tabdate = this.vue3ToolsService.deleteElementsinTable(tabdate);
                CollaboratorCharac = CollaboratorCharac.concat([
                    {
                        NameCollaborator: collaboratorsData[i].NameCollaborator,
                        tableTime: this.vue3ToolsService.ChangeFormatDate(this.vue3ToolsService.TriDateTable(tabdate))
                    }
                ]);
            }
        }
        ProjectsAPI = this.addChargeItemsToTimeline(CollaboratorCharac, ProjectsAPI, k);
        return [ProjectsAPI, CollaboratorCharac, k, this.tabItemsCollaborator];
    }

    public sommeChargeByInterval(visid: any[]): number {
        let chargePerInterval = 0;
        for (let i = 0; i < visid.length; i++) {
            chargePerInterval = chargePerInterval + this.tabItemsCollaborator[visid[i] - 1].charge;
        }
        return chargePerInterval;
    }

    public sommeChargeBySlectedInterval(visid: any[], projectsapi: any[]): number {
        let chargePerInterval = 0;
        for (let i = 0; i < visid.length; i++) {
            chargePerInterval = chargePerInterval + projectsapi[visid[i] - 1].charge;
        }
        return chargePerInterval;
    }
}
