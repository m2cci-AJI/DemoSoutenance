export interface IVue {
    id?: number;
}

export class Vue implements IVue {
    constructor(public id?: number) {}
}
