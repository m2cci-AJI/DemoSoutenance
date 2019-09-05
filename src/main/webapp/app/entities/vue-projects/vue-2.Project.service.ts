import { Injectable } from '@angular/core';
import { SERVER_API_URL } from 'app/app.constants';
import { AffectationsService } from '../../affectations';
import * as moment from 'moment';
import { Vue2ToolsService } from './vue-2.Tools.service';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IVue2 } from 'app/shared/model/vue-2.model';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

type EntityResponseType = HttpResponse<IVue2>;

@Injectable({ providedIn: 'root' })
export class Vue2Service {
    public resourceUrl = SERVER_API_URL + 'api/affectations';
    public tabItemsProject = [];
    public language: string;

    constructor(private affectationsService: AffectationsService,
        protected http: HttpClient,
        private vue2ToolsService: Vue2ToolsService,
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

    public getProjectsToServer() {
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

    public createTimeLineGroupsFromAffectations(StorObject: any) {
        let projectapi = [];
        const image: String = '&#x25B6';
        for (let i = 0; i < StorObject.length; i++) {
            projectapi = projectapi.concat([
                {
                    id: StorObject[i].project.id,
                    NameProject: StorObject[i].project.nameProject,
                    imageLabel: image,
                    content: image + ' ' + StorObject[i].project.nameProject,
                    visible: true,
                    title: 'code: ' + StorObject[i].project.projectCode + ', client: ' + StorObject[i].project.client,
                    subgroupOrder: (a: any, b: any) => {
                        return b.subgroup - a.subgroup;
                    }
                }
            ]);
        }
        this.vue2ToolsService.deleteElementsinTableById(projectapi);
        projectapi.sort(this.tri);
        projectapi = this.addTotalsToGroups(projectapi);
        for (let i = 0; i < projectapi.length; i++) {
            projectapi[i].id = i + 1;
        }
        return projectapi;
    }

    public addTotalsToGroups(ProjectsAPI: any[]) {
        for (let i = 0; i < ProjectsAPI.length; i++) {
            ProjectsAPI.splice(i + 1, 0, {
                id: ProjectsAPI[i].id + 1,
                NameProject: 'Total',
                content: 'Total',
                className: 'gris-ligne',
                visible: false,
                subgroupOrder: (a, b) => {
                    return b.subgroup - a.subgroup;
                }
            });
            i = i + 1;
        }
        return ProjectsAPI;
    }

    public tri(a: any, b: any): number {
        if (a.NameProject < b.NameProject) {
            return -1;
        } else if (a.NameProject === b.NameProject) {
            return 0;
        } else {
            return 1;
        }
    }

    public getCollaboratorsToServerWithoutDetails() {
        return new Promise((resolve, reject) => {
            this.getProjectsToServer().then(
                (resData: any[]) => {
                    const projectsData = resData;
                    this.affectationsService.query().subscribe((res: any) => {
                        const tableResult = this.createTimeLineCollaborators(projectsData, res.body);
                        const CollaboratorsAPI = tableResult[0];
                        resolve(CollaboratorsAPI);
                    });
                },
                (error: any) => reject(error)
            );
        });
    }

    public getIdProjectFromNameProject(NamelabelProject: String, listProject: any[]): number {
        for (let j = 0; j < listProject.length; j++) {
            if (String(NamelabelProject).valueOf() === String(listProject[j].content).valueOf()) {
                return listProject[j].id;
            }
        }
    }

    public selectItembyInterval(CollaboratorsAPI: any[], debutInterval: Date, finInterval: Date, groupId: number): any[] {
        let visid = [];
        for (let i = 0; i < CollaboratorsAPI.length; i++) {
            if (CollaboratorsAPI[i].group === groupId) {
                if (new Date(CollaboratorsAPI[i].start) < finInterval && new Date(CollaboratorsAPI[i].end) > debutInterval) {
                    visid = visid.concat([CollaboratorsAPI[i].id]);
                }
            }
        }
        return visid;
    }

    public addChargeItemsToTimeline(ProjectCharac: any[], CollaboratorsAPI: any[], lastId: number) {
        let l = 0;
        let visid: any[];
        for (let i = 0; i < ProjectCharac.length; i++) {
            for (let j = 0; j < ProjectCharac[i].tableTime.length - 1; j++) {
                visid = [];
                visid = this.selectItembyInterval(
                    CollaboratorsAPI,
                    ProjectCharac[i].tableTime[j],
                    ProjectCharac[i].tableTime[j + 1],
                    2 * i + 1
                );
                CollaboratorsAPI = CollaboratorsAPI.concat([
                    {
                        id: lastId + 1 + l,
                        content: this.sommeChargeBySlectedInterval(visid, CollaboratorsAPI) + '%',
                        className: 'charge',
                        start: ProjectCharac[i].tableTime[j],
                        end: ProjectCharac[i].tableTime[j + 1],
                        group: 2 * i + 2,
                        subgroup: 2 * i + 2,
                        title:
                            '<b>Charge</b> (' +
                            moment(ProjectCharac[i].tableTime[j]).format('MM/DD/YYYY') +
                            ' - ' +
                            moment(ProjectCharac[i].tableTime[j + 1]).format('MM/DD/YYYY') +
                            '): ' +
                            this.sommeChargeByInterval(visid) +
                            '%',
                        editable: false
                    }
                ]);
                l = l + 1;
            }
        }
        return CollaboratorsAPI;
    }

    public createTimeLineCollaborators(projectsData: any[], resbody: any): any[] {
        let storeobj: any[];
        storeobj = resbody;
        let CollaboratorsAPI = [];
        let k = 0;
        let ProjectCharac = [];
        this.tabItemsProject = [];
        for (let i = 0; i < projectsData.length; i++) {
            let tabdate = [];
            if (String(projectsData[i].NameProject).valueOf() !== String('Total').valueOf()) {
                for (let j = 0; j < storeobj.length; j++) {
                    if (String(projectsData[i].NameProject).valueOf() === String(storeobj[j].project.nameProject).valueOf()) {
                        k = k + 1;
                        this.tabItemsProject = this.tabItemsProject.concat([
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
                        CollaboratorsAPI = CollaboratorsAPI.concat([
                            {
                                id: k,
                                NameProject: storeobj[j].project.nameProject,
                                NameCollaborator:
                                    storeobj[j].collaborator.nomCollaborator + ' ' + storeobj[j].collaborator.prenomCollaborator,
                                charge: storeobj[j].charge,
                                content:
                                    '<div class="content"><span class="content-titre"><img src="../../../content/images/account.svg" style="width:20px; height:20px;"> ' +
                                    storeobj[j].collaborator.nomCollaborator +
                                    ' ' +
                                    storeobj[j].collaborator.prenomCollaborator +
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
                                    'style="text-align:center"><img src="../../../content/images/folder-open.svg" style="width:20px; height:20px;"> <b>' +
                                    projectsData[i].NameProject +
                                    '</b> </div> <b>Date</b>: ' +
                                    storeobj[j].dateDebut.format('MM/DD/YYYY') +
                                    ' - ' +
                                    storeobj[j].dateFin.format('MM/DD/YYYY') +
                                    '</br> <b>Name Collaborator</b>: ' +
                                    storeobj[j].collaborator.nomCollaborator +
                                    ' ' +
                                    storeobj[j].collaborator.prenomCollaborator +
                                    '</br> <b>Charge</b>: ' +
                                    storeobj[j].charge +
                                    '%'
                            }
                        ]);
                        tabdate = tabdate.concat([storeobj[j].dateDebut.format('MM/DD/YYYY')]);
                        tabdate = tabdate.concat([storeobj[j].dateFin.format('MM/DD/YYYY')]);
                    }
                }
                tabdate = this.vue2ToolsService.deleteElementsinTable(tabdate);
                ProjectCharac = ProjectCharac.concat([
                    {
                        NameProject: projectsData[i].NameProject,
                        tableTime: this.vue2ToolsService.ChangeFormatDate(this.vue2ToolsService.TriDateTable(tabdate))
                    }
                ]);
            }
        }
        CollaboratorsAPI = this.addChargeItemsToTimeline(ProjectCharac, CollaboratorsAPI, k);
        return [CollaboratorsAPI, ProjectCharac, k, this.tabItemsProject];
    }

    public sommeChargeByInterval(visid: any[]): number {
        let chargePerInterval = 0;
        for (let i = 0; i < visid.length; i++) {
            chargePerInterval = chargePerInterval + this.tabItemsProject[visid[i] - 1].charge;
        }
        return chargePerInterval;
    }

    public sommeChargeBySlectedInterval(visid: any[], collaboratorsapi: any[]): number {
        let chargePerInterval = 0;
        for (let i = 0; i < visid.length; i++) {
            chargePerInterval = chargePerInterval + collaboratorsapi[visid[i] - 1].charge;
        }
        return chargePerInterval;
    }
}
