import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IProjects } from 'app/shared/model/projects.model';

type EntityResponseType = HttpResponse<IProjects>;
type EntityArrayResponseType = HttpResponse<IProjects[]>;

@Injectable({ providedIn: 'root' })
export class ProjectsService {
    public resourceUrl = SERVER_API_URL + 'api/projects';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/projects';

    constructor(protected http: HttpClient) {}

    create(projects: IProjects): Observable<EntityResponseType> {
        return this.http.post<IProjects>(this.resourceUrl, projects, { observe: 'response' });
    }

    update(projects: IProjects): Observable<EntityResponseType> {
        return this.http.put<IProjects>(this.resourceUrl, projects, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IProjects>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IProjects[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IProjects[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
