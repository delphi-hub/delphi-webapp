<template>
  <div>
    <h2 class="w-full text-delphi-red text-2xl font-semibold font-serif">
      <router-link :to="{ name: 'maven-g', params: { groupId: groupId } }">{{
        groupId
      }}</router-link>
      |
      <router-link
        :to="{
          name: 'maven-ga',
          params: { groupId: groupId, artifactId: artifactId },
        }"
        >{{ artifactId }}</router-link
      >
      | {{ version }}
    </h2>

    <div v-if="isLoading">Loading...</div>
    <div v-else class="text-xs line-height-xs">
      <table class="text-left">
        <tr v-if="pom.name != null">
          <th scope="row">Name</th>
          <td>{{ pom.name }}</td>
        </tr>
        <tr v-if="pom.description != null">
          <th scope="row">Description</th>
          <td>{{ pom.description }}</td>
        </tr>
        <tr v-if="pom.parent != null">
          <th scope="row">Parent</th>
          <td>
            <router-link
              :to="{
                name: 'maven-gav',
                params: {
                  groupId: pom.parent.groupId,
                  artifactId: pom.parent.artifactId,
                  version: pom.parent.version,
                },
              }"
              >{{ pom.parent.groupId }} | {{ pom.parent.artifactId }} |
              {{ pom.parent.version }}</router-link
            >
          </td>
        </tr>
        <tr v-if="pom.url != null">
          <th scope="row">URL</th>
          <td>
            <a :href="pom.url">{{ pom.url }}</a>
          </td>
        </tr>
        <tr v-if="pom.licenses != null && pom.licenses.license != null">
          <th scope="row">License</th>
          <td>
            <a :href="pom.licenses.license.url">{{
              pom.licenses.license.name
            }}</a>
          </td>
        </tr>
      </table>

      <table
        class="text-left"
        v-if="
          pom.dependencies != null &&
          pom.dependencies.dependency != null &&
          pom.dependencies.dependency.length > 0
        "
      >
        <caption class="text-left font-bold">
          Dependencies
        </caption>
        <tr>
          <th scope="col">Name</th>
          <th scope="col">Scope</th>
        </tr>
        <tr v-for="(d, index) in pom.dependencies.dependency" :key="index">
          <td>
            <router-link
              :to="{
                name: 'maven-gav',
                params: {
                  groupId: d.groupId,
                  artifactId: d.artifactId,
                  version: d.version,
                },
              }"
              >{{ d.groupId }} | {{ d.artifactId }} |
              {{ d.version }}</router-link
            >
          </td>
          <td>{{ d.scope }}</td>
        </tr>
      </table>

      <table class="text-left" v-if="pom.properties != null">
        <caption class="text-left font-bold">
          Properties
        </caption>
        <tr>
          <th scope="col">Key</th>
          <th scope="col">Value</th>
        </tr>
        <tr v-for="(value, key) in pom.properties" :key="key">
          <td>{{ key }}</td>
          <td>{{ value }}</td>
        </tr>
      </table>
    </div>
  </div>
</template>
<script lang="ts">
import { defineComponent, onBeforeMount, ref, watch } from "vue";
import { useRoute } from "vue-router";

import { MavenAccess } from '@/backend/maven/Maven';

export default defineComponent({
  setup() {
    const route = useRoute();
    const groupId = ref("");
    const artifactId = ref("");
    const version = ref("");

    const isLoading = ref(false);
    const pom = ref(null);

    onBeforeMount(async () => {
      processRouteValues();
      await loadPom();
    });

    watch(
      () => route.params,
      async () => {
        processRouteValues();
        await loadPom();
      }
    );

    async function loadPom() {
      if (groupId.value === undefined || 
          artifactId.value === undefined || 
          version.value == undefined) return;
      isLoading.value = true;
      pom.value = await new MavenAccess().getArtifactVersion(groupId.value, artifactId.value, version.value);
      isLoading.value = false;
    }

    function processRouteValues(): void {
      groupId.value = route.params.groupId as string;
      artifactId.value = route.params.artifactId as string;
      version.value = route.params.version as string;
    }

    return { groupId, artifactId, version, isLoading, pom };
  },
});
</script>
