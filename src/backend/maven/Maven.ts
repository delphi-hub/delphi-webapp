import { XMLParser } from "fast-xml-parser";

export class MavenAccess {
    async getArtifactVersion(groupId: string, artifactId: string, version: string): Promise<any> {
        const url = `/maven2/${groupId.replaceAll(".", "/")}/${artifactId
            }/${version}/${artifactId}-${version}.pom`;

        return fetch(url)
            .then((response) => response.text())
            .then((str) => {
                const parser = new XMLParser();
                return parser.parse(str).project;
            })
            .catch((reason) => console.error(reason));
    }

    private async retrieveAndProcessDirectory(url: string): Promise<any> {
        return fetch(url)
            .then((response) => response.text())
            .then((text) => {
                const parser = new DOMParser();
                return Array.from(parser.parseFromString(text, "text/html")
                    .getElementById("contents")
                    ?.querySelectorAll("A")!)
                    .slice(1).map(a => a.textContent)
                    .filter(link => link?.endsWith("/"))
                    .map(link => link?.replace("/", ""));
            })
            .catch((reason) => console.error(reason));
    }

    async getArtifact(groupId: string, artifactId: string): Promise<any> {
        return this.retrieveAndProcessDirectory(`/maven2/${groupId.replaceAll(".", "/")}/${artifactId}/`);
    }

    async getGroup(groupId: string): Promise<any> {
        return this.retrieveAndProcessDirectory(`/maven2/${groupId.replaceAll(".", "/")}/`);
    }


}