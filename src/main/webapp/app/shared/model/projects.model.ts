import { IAffectations } from 'app/shared/model/affectations.model';

export interface IProjects {
    id?: number;
    nameProject?: string;
    projectCode?: string;
    client?: string;
    dP?: string;
    description?: string;
    affectations?: IAffectations[];
}

export class Projects implements IProjects {
    constructor(
        public id?: number,
        public nameProject?: string,
        public projectCode?: string,
        public client?: string,
        public dP?: string,
        public description?: string,
        public affectations?: IAffectations[]
    ) {}
}
