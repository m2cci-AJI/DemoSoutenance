import { Moment } from 'moment';
import { ICollaborators, Collaborators } from 'app/shared/model/collaborators.model';
import { IProjects, Projects } from 'app/shared/model/projects.model';
import { IdType } from 'vis';

export enum Colors {
    vert = 'vert',
    rouge = 'rouge',
    gris = 'gris'
}

export interface IAffectations {
    id?: number;
    dateDebut?: Moment;
    dateFin?: Moment;
    charge?: number;
    commentaire?: string;
    color?: Colors;
    collaborator?: ICollaborators;
    project?: IProjects;
}

export class Affectations implements IAffectations {
    constructor(
        public id?: number,
        public dateDebut?: Moment,
        public dateFin?: Moment,
        public charge?: number,
        public commentaire?: string,
        public color?: Colors,
        public collaborator?: ICollaborators,
        public project?: IProjects
    ) {
        this.collaborator = new Collaborators();
        this.project = new Projects();
    }
}
