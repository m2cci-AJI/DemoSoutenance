import { Component, OnInit, OnDestroy, ViewChild, TemplateRef } from '@angular/core';
import {
    VisTimelineService,
    VisTimelineItems,
    VisTimelineGroups,
    VisId,
    VisDate,
    VisTimelineOptions,
    VisTimelineItem,
    VisTimelineGroup
} from './components/timeline';
import * as _swal from 'sweetalert';
import { SweetAlert } from 'sweetalert/typings/core';
import { Vue2ToolsService } from './vue-2.Tools.service';
const swal: SweetAlert = _swal as any;
import { Vue2Service } from '.';
import { AffectationsService } from '../../affectations/affectations.service';
import { IAffectations, Affectations, Colors } from '../../shared/model/affectations.model';
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { CollaboratorsService } from '../../collaborators';
import { FormArray, FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { Subscription } from 'rxjs/internal/Subscription';
import { AccountService } from 'app/core';
import { IProjects } from 'app/shared/model/projects.model';
import { ProjectsService } from '../../projects';
import { ICollaborators } from 'app/shared/model/collaborators.model';
@Component({
    selector: 'jhi-vue-2',
    templateUrl: './vue-2.component.html',
    styleUrls: ['./vue-2.component.css']
})
export class Vue2Component implements OnInit, OnDestroy {
    public visTimeline = 'MyFirstTimeline';
    public visTimelineItems: VisTimelineItems;
    public infosItemCollaborator: VisTimelineItem;
    public infosItemProject: VisTimelineItem;
    public visTimelineGroups: VisTimelineGroups;
    public dateDebutItem: VisDate;
    public dateFinItem: VisDate;
    public groupItem: Number = undefined;
    public iditem: VisId = null;
    public closeResult: String;
    public userFormCollab: FormGroup;
    public userFormProject: FormGroup;
    public userFormAffect: FormGroup;
    public collaborator1: any;
    public project1: any;
    public collaboratorSelected: any;
    public ProjectdoubleClickSubscription: Subscription;
    public ProjectclickSubscription: Subscription;
    public ProjectchangedSubscription: Subscription;
    public ProjectitemoverSubscription: Subscription;
    public importcollaboratorAPIwithTotals: any[] = [];
    public importproject: any[];
    @ViewChild('content5') templateRef: TemplateRef<any>;
    public affectationChange: IAffectations = new Affectations();
    public oncetimeupdate: Boolean = false;
    public visTimelineOptionsAdmin: VisTimelineOptions = {
        snap: (date: any, scale, step) => {
            const hour = 24 * 60 * 60 * 1000;
            return Math.round(date / hour) * hour;
        },
        onRemove: (item, callback) => {
            swal({
                title: "Supression de l'affectation",
                text: 'Êtes-vous sûr de vouloir supprimer cette affectation ?',
                icon: 'warning',
                confirmButtonClass: 'btn-outline-info',
                dangerMode: true,
                closeOnClickOutside: false,
                buttons: {
                    cancel: 'Annuler',
                    confirm: 'Supprimer'
                }
            } as any).then(function(ok) {
                if (ok) {
                    callback(item); // confirm deletion
                } else {
                    callback(null); // cancel deletion
                }
            });
        },
        orientation: {
            axis: 'top',
            item: 'top'
        },
        editable: true,
        selectable: true,
        stack: false,
        tooltipOnItemUpdateTime: false,
        clickToUse: false
    };
    public visTimelineOptionsUser: VisTimelineOptions = {
        orientation: {
            axis: 'top',
            item: 'top'
        },
        editable: false,
        selectable: true,
        stack: false,
        tooltipOnItemUpdateTime: false,
        clickToUse: false
    };
    public settingsAccountlogin: any;
    public itemsTimeline: any[];
    public allProjects: IProjects[];
    public allCollaborators: ICollaborators[];
    public shortTimeline: Boolean;
    public mementos: any[] = [];
    public iterationHistory: number = (-1);
    public OutUndoHistory: Boolean = true;
    public OutRedoHistory: Boolean = true;
    public lastIteration: number = (-100); // arbitary value

    public constructor(
        private visTimelineService: VisTimelineService,
        private vue2s: Vue2Service,
        private affectationservice: AffectationsService,
        private modalService: NgbModal,
        protected collaboratorsService: CollaboratorsService,
        protected jhiAlertService: JhiAlertService,
        private formBuilder: FormBuilder,
        private accountService: AccountService,
        protected projectsService: ProjectsService,
        private vue2ToolsService: Vue2ToolsService
    ) {
        this.accountService.identity().then(account => {
            this.settingsAccountlogin = account.login;
        });

        this.affectationservice.query().subscribe((res: any) => {
            let importproject: any[] = [];
            let allCollaborators: any[] = [];
            let allProjects: any[] = [];
            for (let i = 0; i < res.body.length; i++) {
                importproject = importproject.concat([
                    {
                        id: res.body[i].project.id,
                        content: res.body[i].project.nameProject
                    }
                ]);
            }
            this.importproject = importproject;
            for (let i = 0; i < res.body.length; i++) {
                allCollaborators = allCollaborators.concat([res.body[i].collaborator]);
                allProjects = allProjects.concat([res.body[i].project]);
            }
            this.vue2ToolsService.deleteElementsinTableById(allCollaborators);
            this.vue2ToolsService.deleteElementsinTableById(allProjects);
            allCollaborators.sort(this.vue2ToolsService.triBynomCollaborator);
            allProjects.sort(this.vue2ToolsService.triBynameProject);
            this.allCollaborators = allCollaborators;
            this.allProjects = allProjects;
        });
    }

    public ngOnInit(): void {
        this.ViewWithProjects(false);
    }

    public ViewWithProjects(isNotFirst: Boolean) {
        this.vue2s
            .getProjectsToServer()
            .then((resData1: any[]) => {
                const tabProjectCharge = resData1;
                this.visTimelineGroups = new VisTimelineGroups(tabProjectCharge);
                return this.vue2s.getCollaboratorsToServerWithoutDetails();
            })
            .then((resData2: any[]) => {
                this.visTimelineItems = new VisTimelineItems(resData2);
                this.shortTimeline = false;
                this.itemsTimeline = this.visTimelineItems.getAll();
                this.importcollaboratorAPIwithTotals = resData2;

                if (!isNotFirst) {
                    this.saveMemento();
                    this.captureSimpleClickViewWithProjects();
                    this.captureItemUpdateViewWithProjects();
                    this.captureItemOverViewWithProjects();
                    this.captureDoubleClickViewWithProjects();
                }
            });
    }

    public captureItemOverViewWithProjects() {
        setTimeout(() => {
            this.visTimelineService.on(this.visTimeline, 'itemover');
            this.ProjectitemoverSubscription = this.visTimelineService.itemover.subscribe((eventDataC3: any[]) => {
                this.infosItemProject = this.visTimelineItems.getById(eventDataC3[1].item);
                const infosGroupProject = this.visTimelineGroups.getById(this.infosItemProject.group);
                if (
                    this.iditem !== this.infosItemProject.id &&
                    String(infosGroupProject.content).valueOf() !== String('Total').valueOf() &&
                    String(typeof this.infosItemProject.id).valueOf() !== String('string').valueOf()
                ) {
                    this.groupItem = this.infosItemProject.group;
                    this.iditem = this.infosItemProject.id;
                    this.oncetimeupdate = false;
                }
            });
        }, 40);
    }

    public captureSimpleClickViewWithProjects() {
        setTimeout(() => {
            this.visTimelineService.on(this.visTimeline, 'click');
            this.ProjectclickSubscription = this.visTimelineService.click.subscribe((eventDataP1: any[]) => {
                const idGroup = eventDataP1[1].group + 1;
                const detectClick = eventDataP1[1].what;
                const infosGroupProject = this.visTimelineGroups.getById(idGroup - 1);
                if (
                    String(detectClick).valueOf() === String('group-label').valueOf() &&
                    String(Object(infosGroupProject).NameProject).valueOf() !== String('Total').valueOf()
                ) {
                    this.changeVisibilityLabel(idGroup);
                    this.changeImageLabel(idGroup - 1, infosGroupProject);
                    this.visTimelineGroups.update(this.visTimelineGroups.getByIds([idGroup - 1, idGroup]));
                }
            });
        }, 10);
    }

    public changeImageLabel(idGroup: number, visTimelineGroup: VisTimelineGroup) {
        if (Object(this.visTimelineGroups)._data[idGroup].imageLabel === '&#x25B6') {
            Object(this.visTimelineGroups)._data[idGroup].content = '&#x25bc' + ' ' + Object(visTimelineGroup).NameProject;
            Object(this.visTimelineGroups)._data[idGroup].imageLabel = '&#x25bc';
        } else {
            Object(this.visTimelineGroups)._data[idGroup].content = '&#x25B6' + ' ' + Object(visTimelineGroup).NameProject;
            Object(this.visTimelineGroups)._data[idGroup].imageLabel = '&#x25B6';
        }
    }

    public changeVisibilityLabel(idGroup: number) {
        if (Object(this.visTimelineGroups)._data[idGroup].visible === true) {
            Object(this.visTimelineGroups)._data[idGroup].visible = false;
        } else {
            Object(this.visTimelineGroups)._data[idGroup].visible = true;
        }
    }

    public captureDoubleClickViewWithProjects() {
        setTimeout(() => {
            this.visTimelineService.on(this.visTimeline, 'doubleClick');
            this.ProjectdoubleClickSubscription = this.visTimelineService.doubleClick.subscribe((eventDataP2: any[]) => {
                const infosItemProject = this.visTimelineItems.getById(eventDataP2[1].item);
                if (this.verifyLogin()) {
                    if (
                        Object.keys(eventDataP2[1]).length === 2 &&
                        String(Object(infosItemProject).NameProject).valueOf() !== String('Total').valueOf()
                    ) {
                        this.affectationChange.id = this.vue2s.tabItemsProject[eventDataP2[1].item - 1].idAffectation;
                        this.affectationChange.collaborator.id = this.vue2s.tabItemsProject[eventDataP2[1].item - 1].idCollaborator;
                        this.affectationChange.project.id = this.vue2s.tabItemsProject[eventDataP2[1].item - 1].idProject;
                        this.affectationChange.charge = this.vue2s.tabItemsProject[eventDataP2[1].item - 1].charge;
                        this.affectationChange.dateDebut = moment(infosItemProject.start);
                        this.affectationChange.dateFin = moment(infosItemProject.end);
                        this.affectationChange.color = Colors[infosItemProject.className];
                        this.open(this.templateRef);
                    } else if (Object.keys(eventDataP2[1]).length > 2) {
                        this.visTimelineItems.update(this.visTimelineItems.getAll());
                    }
                }
            });
        }, 40);
    }

    public captureItemUpdateViewWithProjects() {
        setTimeout(() => {
            this.visTimelineService.on(this.visTimeline, 'changed');
            this.ProjectchangedSubscription = this.visTimelineService.changed.subscribe((eventDataP5: any[]) => {
                const actualitemsTimeline = this.visTimelineItems.getAll();
                // delete affectation from the timeline by selecting x near its
                if (actualitemsTimeline.length < this.itemsTimeline.length && !this.shortTimeline) {
                    let itemDeleted: Number;
                    for (let i = 0; i < this.itemsTimeline.length; i++) {
                        let itemN = 0;
                        for (let j = 0; j < actualitemsTimeline.length; j++) {
                            if (this.itemsTimeline[i].id !== actualitemsTimeline[j].id) {
                                itemN = itemN + 1;
                            }
                        }
                        if (itemN === actualitemsTimeline.length) {
                            itemDeleted = this.itemsTimeline[i].id;
                        }
                    }

                    for (let i = 0; i < this.vue2s.tabItemsProject.length; i++) {
                        if (this.vue2s.tabItemsProject[i].idItem === itemDeleted) {
                            this.DeleteAffectationBySelectedID(this.vue2s.tabItemsProject[i].idAffectation);
                        }
                    }
                    this.itemsTimeline = actualitemsTimeline;
                } else {
                    // update the affectation with changing its place in the timeline
                    if (this.iditem !== null && this.groupItem !== undefined) {
                        const actualinfosItemProject = this.visTimelineItems.getById(this.iditem);
                        if (actualinfosItemProject !== null) {
                            const affectationProject: IAffectations = new Affectations();
                            affectationProject.id = this.vue2s.tabItemsProject[Number(this.iditem) - 1].idAffectation;
                            affectationProject.dateDebut = moment(actualinfosItemProject.start);
                            affectationProject.dateFin = moment(actualinfosItemProject.end);
                            if (this.groupItem === actualinfosItemProject.group) {
                                affectationProject.project.id = this.vue2s.tabItemsProject[Number(this.iditem) - 1].idProject;
                            } else {
                                affectationProject.project.id = this.GiveIdProjectFromGroupItem(actualinfosItemProject.group);
                            }
                            affectationProject.collaborator.id = this.vue2s.tabItemsProject[Number(this.iditem) - 1].idCollaborator;
                            affectationProject.charge = this.vue2s.tabItemsProject[Number(this.iditem) - 1].charge;
                            affectationProject.color = Colors[actualinfosItemProject.className];
                            if (!this.oncetimeupdate) {
                                if (
                                    String(affectationProject.dateDebut).valueOf() !==
                                        String(moment(this.infosItemProject.start)).valueOf() ||
                                    String(affectationProject.dateFin).valueOf() !== String(moment(this.infosItemProject.end)).valueOf() ||
                                    this.groupItem !== actualinfosItemProject.group
                                ) {
                                    this.updateDeadlineToServer(affectationProject);
                                    this.oncetimeupdate = true;
                                }
                            }
                        }
                    }
                }
            });
        }, 40);
    }

    public open(content) {
        this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' }).result.then(
            result => {
                this.closeResult = `Closed with: ${result}`;
            },
            reason => {
                this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
            }
        );
    }

    public DeleteAffectationBySelectedID(id: number) {
        this.deleteAffectationfromServer(id).subscribe((res: any) => {
            this.saveMemento();
        });
    }

    public deleteAffectationfromServer(id: number) {
        return this.affectationservice.delete(id);
    }

    public GiveIdProjectFromGroupItem(idGroup: Number): number {
        for (let i = 0; i < this.importcollaboratorAPIwithTotals.length; i++) {
            if (this.importcollaboratorAPIwithTotals[i].group === idGroup) {
                return this.vue2s.getIdProjectFromNameProject(this.importcollaboratorAPIwithTotals[i].NameProject, this.importproject);
            }
        }
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    public updateDeadlineToServer(affectation: IAffectations) {
        return this.affectationservice.update(affectation).subscribe((res: any) => {
            this.saveMemento();
            setTimeout(() => {
                this.ViewWithProjects(true);
            }, 40);
            this.iditem = null;
        });
    }

    public Centertimeline() {
        const currenttime = this.visTimelineService.getCurrentTime(this.visTimeline);
        this.visTimelineService.moveTo(this.visTimeline, currenttime);
    }

    public Ajusttimeline() {
        this.visTimelineService.fit(this.visTimeline);
    }

    public initializeFormCollab() {
        this.userFormCollab = this.formBuilder.group({
            collaborator1: new FormControl('', Validators.required),
            AddCollaboratorList: this.formBuilder.array([])
        });
    }

    public initializeFormProject() {
        this.userFormProject = this.formBuilder.group({
            project1: new FormControl('', Validators.required),
            AddProjectList: this.formBuilder.array([])
        });
    }

    public initializeFormAffect() {
        this.userFormAffect = this.formBuilder.group({
            project1: new FormControl('', Validators.required),
            collaborator1: new FormControl('', Validators.required)
        });
    }

    public openAffect(content) {
        this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' }).result.then(
            result => {
                this.closeResult = `Closed with: ${result}`;
            },
            reason => {
                this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
            }
        );
        this.initializeFormAffect();
    }

    public openCollab(content) {
        this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' }).result.then(
            result => {
                this.closeResult = `Closed with: ${result}`;
            },
            reason => {
                this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
            }
        );
        this.initializeFormCollab();
    }

    public openProject(content) {
        this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title' }).result.then(
            result => {
                this.closeResult = `Closed with: ${result}`;
            },
            reason => {
                this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
            }
        );
        this.initializeFormProject();
    }

    public trackCollaboratorsById(index: Number, item: any) {
        return item.id;
    }

    public trackProjectsById(index: Number, item: any) {
        return item.id;
    }

    public DisplaySomeCollaborators() {
        let id: VisId[] = [];
        const tabCollaborator = this.getCollaborators().value.concat([this.collaborator1]);
        for (const i in Object(this.visTimelineItems)._data) {
            if (Object(this.visTimelineItems)._data.hasOwnProperty(i)) {
                let notvisible: Boolean;
                for (let j = 0; j < tabCollaborator.length; j++) {
                    if (
                        String(Object(this.visTimelineItems)._data[i].NameCollaborator).valueOf() ===
                        String(tabCollaborator[j].nomCollaborator + ' ' + tabCollaborator[j].prenomCollaborator).valueOf()
                    ) {
                        notvisible = true;
                    } else {
                        notvisible = false;
                    }
                    if (notvisible) {
                        id = id.concat(Object(this.visTimelineItems)._data[i].id);
                    }
                }
            }
        }
        const newVisTimelineItems: VisTimelineItems = this.visTimelineItems.getByIds(id) as any;
        this.visTimelineService.setItems(this.visTimeline, newVisTimelineItems);
        this.iditem = null;
    }

    public DisplaySomeProjects() {
        const tabProject = this.getProjects().value.concat([this.project1]);
        for (const i in Object(this.visTimelineGroups)._data) {
            if (Object(this.visTimelineGroups)._data.hasOwnProperty(i)) {
                let result: Boolean = true;
                let notvisible: Boolean;
                for (let j = 0; j < tabProject.length; j++) {
                    if (
                        String(Object(this.visTimelineGroups)._data[i].NameProject).valueOf() !==
                        String(tabProject[j].nameProject).valueOf()
                    ) {
                        notvisible = true;
                    } else {
                        notvisible = false;
                    }
                    result = result && notvisible;
                }
                if (result) {
                    Object(this.visTimelineGroups)._data[i].visible = false;
                } else {
                    Object(this.visTimelineGroups)._data[i].visible = true;
                }
            }
        }
        this.visTimelineGroups.update(this.visTimelineGroups.getByIds(this.visTimelineGroups.getIds()));
        this.iditem = null;
    }

    public DisplaySomeAffectations() {
        let id: VisId = 0;
        const tabCollaborator = [this.collaborator1];
        const tabProject = [this.project1];
        for (const i in Object(this.visTimelineItems)._data) {
            if (Object(this.visTimelineItems)._data.hasOwnProperty(i)) {
                if (String(Object(this.visTimelineItems)._data[i].NameProject).valueOf() === String(tabProject[0].nameProject).valueOf()) {
                    if (
                        String(Object(this.visTimelineItems)._data[i].NameCollaborator).valueOf() ===
                        String(tabCollaborator[0].nomCollaborator + ' ' + tabCollaborator[0].prenomCollaborator).valueOf()
                    ) {
                        id = Object(this.visTimelineItems)._data[i].id;
                        const newVisTimelineItems: VisTimelineItems = this.visTimelineItems.getByIds([id]) as any;
                        this.visTimelineService.setItems(this.visTimeline, newVisTimelineItems);
                    }
                }
            }
        }
        if (id === 0) {
            if (String(this.vue2s.language).valueOf() === String('fr').valueOf()) {
                swal({
                    title: "Affichage d'affectation",
                    text: "Cette affectation n'existe pas dans la timeline !",
                    icon: 'warning',
                    confirmButtonClass: 'btn-outline-info',
                    dangerMode: true,
                    closeOnClickOutside: false,
                    showCloseButton: true
                } as any);
            } else {
                swal({
                    title: 'Display of affectation',
                    text: "This affectation doesn't exist in the timeline !",
                    icon: 'warning',
                    confirmButtonClass: 'btn-outline-info',
                    dangerMode: true,
                    closeOnClickOutside: false,
                    showCloseButton: true
                } as any);
            }
        }
        this.iditem = null;
    }

    public DisplayAllAffectations() {
        for (const i in Object(this.visTimelineGroups)._data) {
            if (
                !Object(this.visTimelineGroups)._data[i].visible &&
                String(Object(this.visTimelineGroups)._data[i].NameProject).valueOf() !== String('Total').valueOf()
            ) {
                Object(this.visTimelineGroups)._data[i].visible = true;
            }
        }
        this.visTimelineService.setData(this.visTimeline, { groups: this.visTimelineGroups, items: this.visTimelineItems });
        this.iditem = null;
    }

    public ActualizeTimeline() {
        if (this.mementos[this.lastIteration].length === this.mementos[this.iterationHistory].length) {
            for (let i = 0; i < this.mementos[this.iterationHistory].length; i++) {
                this.affectationservice.update(this.mementos[this.iterationHistory][i]).subscribe((res: any) => {
                    this.ViewWithProjects(true);
                });
            }
        } else if (this.mementos[this.lastIteration].length < this.mementos[this.iterationHistory].length) {
            this.affectationservice
                .update(this.ActualIdAffectation(this.mementos[this.iterationHistory], this.mementos[this.lastIteration])[1])
                .subscribe((res: any) => {
                    for (let i = 0; i < this.mementos[this.iterationHistory].length; i++) {
                        if (
                            this.mementos[this.iterationHistory][i].id ===
                            this.ActualIdAffectation(this.mementos[this.iterationHistory], this.mementos[this.lastIteration])[0]
                        ) {
                            this.mementos[this.iterationHistory][i] = res.body;
                        }
                    }
                    this.ViewWithProjects(true);
                });
        } else {
            this.affectationservice
                .delete(this.ActualIdAffectation(this.mementos[this.lastIteration], this.mementos[this.iterationHistory])[0])
                .subscribe((res: any) => {
                    this.ViewWithProjects(true);
                });
        }
    }

    public ActualIdAffectation(BigTable: any[], SmallTable: any[]) {
        let param: Boolean;
        for (let i = 0; i < BigTable.length; i++) {
            param = false;
            let j = 0;
            while (j < SmallTable.length && !param) {
                if (BigTable[i].id === SmallTable[j].id) {
                    param = true;
                }
                j = j + 1;
            }
            if (!param && j === SmallTable.length) {
                return [BigTable[i].id, BigTable[i]];
            }
        }
    }

    public OutHistory() {
        if (this.iterationHistory <= 0) {
            this.OutUndoHistory = true;
            this.OutRedoHistory = false;
        }
        if (this.iterationHistory >= this.mementos.length - 1) {
            this.OutRedoHistory = true;
            this.OutUndoHistory = false;
        }
    }

    public UndoTimeline() {
        this.iditem = null;
        if (this.iterationHistory <= 0) {
            return;
        } else {
            this.lastIteration = this.iterationHistory;
            this.iterationHistory = this.iterationHistory - 1;
            this.OutHistory();
        }
        this.ActualizeTimeline();
    }

    public RedoTimeline() {
        this.iditem = null;
        if (this.iterationHistory >= this.mementos.length - 1) {
            return;
        } else {
            this.lastIteration = this.iterationHistory;
            this.iterationHistory = this.iterationHistory + 1;
            this.OutHistory();
        }
        this.ActualizeTimeline();
    }

    public saveMemento() {
        if (this.iterationHistory < this.mementos.length - 1) {
            this.mementos.splice(this.iterationHistory + 1, this.mementos.length - this.iterationHistory);
            this.OutRedoHistory = true;
        }
        this.affectationservice.query().subscribe((res: any) => {
            let storeobj: any[];
            storeobj = res.body;
            if (this.mementos.length >= 2) {
                this.RefreshTableHistory();
                this.mementos[1] = res.body;
            } else {
                this.mementos.push(storeobj);
                this.iterationHistory = this.iterationHistory + 1;
            }
            if (this.iterationHistory >= 1) {
                this.OutUndoHistory = false;
            }
        });
    }

    public RefreshTableHistory() {
        for (let i = 0; i < this.mementos.length - 1; i++) {
            this.mementos[i] = this.mementos[i + 1];
        }
    }

    public getCollaborators(): FormArray {
        return this.userFormCollab.get('AddCollaboratorList') as FormArray;
    }

    public getProjects(): FormArray {
        return this.userFormProject.get('AddProjectList') as FormArray;
    }

    public getAffectations(): FormArray {
        return this.userFormAffect.get('AddAffectationList') as FormArray;
    }

    public AddCollaborators() {
        const newCollaboratorControl = this.formBuilder.control(null, Validators.required);
        this.getCollaborators().push(newCollaboratorControl);
    }

    public AddProjects() {
        const newProjectControl = this.formBuilder.control(null, Validators.required);
        this.getProjects().push(newProjectControl);
    }

    public displayInterval() {
        this.visTimelineService.setWindow(this.visTimeline, this.dateDebutItem, this.dateFinItem);
    }

    private getDismissReason(reason: any): String {
        if (reason === ModalDismissReasons.ESC) {
            return 'by pressing ESC';
        } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
            return 'by clicking on a backdrop';
        } else {
            return `with: ${reason}`;
        }
    }

    public changeInputsAffectations() {
        this.affectationChange.collaborator.id = this.collaboratorSelected.id;
        this.updateDeadlineToServer(this.affectationChange);
    }

    public AddTotalToList(list: any[]) {
        let j = 2;
        for (let i = 0; i < list.length; i++) {
            list.splice(i + 1, 0, { id: j, content: 'Total' });
            i = i + 1;
            j = j + 2;
        }
        for (let i = 0; i < list.length; i++) {
            list[i].id = i + 1;
        }
        return list;
    }

    public verifyLogin(): Boolean {
        return String(this.settingsAccountlogin).valueOf() === String('admin').valueOf();
    }

    public traductionTitle() {
        const titleEn1: String = 'Display collaborator(s)';
        const titleFr1: String = 'Afficher un(des) collaborator(s)';
        const titleEn2: String = 'Display project(s)';
        const titleFr2: String = 'Afficher un(des) projet(s)';
        const titleEn3: String = 'Display affectation';
        const titleFr3: String = 'Afficher une affectation';
        const titleEn4: String = 'Display all affectations';
        const titleFr4: String = 'Afficher toutes les affectation';
        const titleEn5: String = 'Center the timeline';
        const titleFr5: String = 'Centrer la timeline';
        const titleEn6: String = 'Adjust the timeline';
        const titleFr6: String = 'Ajuster la timeline';
        const titleEn7: String = "Adjust the timeline on time's interval";
        const titleFr7: String = 'Ajuster la timeline sur un intervalle de temps';
        const titleEn8: String = 'Click to go back';
        const titleFr8: String = 'Cliquer pour revenir en arrière';
        const titleEn9: String = 'Click to advance';
        const titleFr9: String = 'Cliquer pour avancer';
        if (String(this.vue2s.language).valueOf() === String('fr').valueOf()) {
             return [titleFr1, titleFr2, titleFr3, titleFr4, titleFr5, titleFr6, titleFr7, titleFr8, titleFr9];
        } else {
            return [titleEn1, titleEn2, titleEn3, titleEn4, titleEn5, titleEn6, titleEn7, titleEn8, titleEn9];
        }
    }

    public ngOnDestroy() {
        this.ProjectdoubleClickSubscription.unsubscribe();
        this.ProjectclickSubscription.unsubscribe();
        this.ProjectchangedSubscription.unsubscribe();
        this.ProjectitemoverSubscription.unsubscribe();
    }
}
